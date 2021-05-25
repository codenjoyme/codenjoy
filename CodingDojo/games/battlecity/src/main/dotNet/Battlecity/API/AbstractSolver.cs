/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2020 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Authentication;
using System.Text;
using System.Threading.Tasks;

using WebSocketSharp;

namespace Battlecity.API
{
    public abstract class AbstractSolver
    {
        const string ResponsePrefix = "board=";
        const int MaxRetriesCount = 3;
        const int RetriestTimeoutInMilliseconds = 10000;

        readonly string webSocketUrl;
        int retriesCount;
        bool shouldExit;
        WebSocket gameServer;

        public AbstractSolver(string serverUrl) =>
            webSocketUrl = GetWebSocketUrl(serverUrl);

        public void Play()
        {
            gameServer = new WebSocket(webSocketUrl);
            gameServer.SslConfiguration.EnabledSslProtocols = SslProtocols.Tls12;

            gameServer.OnMessage += Socket_OnMessage;
            gameServer.OnClose += async (server, closeEventArgs) => 
                await ReconnectAsync(closeEventArgs.WasClean, closeEventArgs.Code);

            gameServer.Connect();
        }

        public void InitiateExit()
        {
            Console.WriteLine("Exit initiated...");

            shouldExit = true;

            if (gameServer.ReadyState == WebSocketState.Open)
                gameServer.Close();
        }

        protected internal abstract string Get(Board gameBoard);

        protected internal string GetWebSocketUrl(string serverUrl) =>
            serverUrl.Replace("http", "ws").Replace("board/player/", "ws?user=").Replace("?code=", "&code=");

        void Socket_OnMessage(object sender, MessageEventArgs messageEventArgs)
        {
            if (!shouldExit)
            {
                string response = messageEventArgs.Data;
                retriesCount = default;

                if (!response.StartsWith(ResponsePrefix))
                {
                    Console.WriteLine("Something strange is happening on the server... Response:\n{0}", response);
                    InitiateExit();
                }
                else
                {
                    string boardString = response.Substring(ResponsePrefix.Length);
                    var board = new Board(boardString);

                    Console.Clear();
                    Console.SetCursorPosition(0, 0);
                    Console.WriteLine(board.ToString());

                    var action = Get(board);

                    Console.WriteLine("Answer: " + action);
                    Console.SetCursorPosition(0, 0);

                    ((WebSocket)sender).Send(action);
                }
            }
        }

        async Task ReconnectAsync(bool wasClean, ushort code)
        {
            if (!wasClean && !gameServer.IsAlive && IsAllowedToReconnect(code))
            {
                if (retriesCount < MaxRetriesCount)
                {
                    Console.WriteLine($"Trying to recconnect, attempt {retriesCount + 1} of {MaxRetriesCount}...");
                    await Task.Delay(RetriestTimeoutInMilliseconds);

                    retriesCount++;
                    gameServer.Connect();
                }
                else
                    Console.WriteLine("Could not reconnect to the server, please try again later. Press any key to exit...");
            }
        }

        bool IsAllowedToReconnect(ushort code) => 
            new List<ushort>() { 1006, 1011 }.Contains(code);
    }
}
