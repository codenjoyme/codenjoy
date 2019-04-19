/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
using System.Web;
using System.Linq;
using System.Threading;
using System.Text;
using WebSocketSharp;

namespace A2048.Api
{
    public abstract class AbstractSolver
    {
        private const string ResponsePrefix = "board=";

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="server">server http address including email and code</param>
        public AbstractSolver(string server)
        {
            // Console.OutputEncoding = Encoding.UTF8;
            ServerUrl = server;
        }

        public string ServerUrl { get; private set; }
        

        /// <summary>
        /// Set this property to true to finish playing
        /// </summary>
        public bool ShouldExit { get; protected set; }

        public void Play()
        {
            string url = GetWebSocketUrl(this.ServerUrl);

            var socket = new WebSocket(url);

            socket.OnMessage += Socket_OnMessage;
            socket.Connect();

            while (!ShouldExit && socket.ReadyState != WebSocketState.Closed)
            {
                Thread.Sleep(50);
            }
        }


        private void Socket_OnMessage(object sender, MessageEventArgs e)
        {
            if (!ShouldExit)
            {
                var response = e.Data;

                if (!response.StartsWith(ResponsePrefix))
                    {
                        Console.WriteLine("Something strange is happening on the server... Response:\n{0}", response);
                        ShouldExit = true;
                    }
                    else
                    {
                        var boardString = response.Substring(ResponsePrefix.Length);
                        var board = new Board(boardString);

                        //Just print current state (gameBoard) to console
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

        public static string GetWebSocketUrl(string serverUrl)
        {
            Uri uri = new Uri(serverUrl);

            var server = $"{uri.Host}:{uri.Port}";
            var userName = uri.Segments.Last();
            var code = HttpUtility.ParseQueryString(uri.Query).Get("code");

            return GetWebSocketUrl(userName, code, server);
        }

        private static string GetWebSocketUrl(string userName, string code, string server)
        {
            return string.Format("ws://{0}/codenjoy-contest/ws?user={1}&code={2}",
                            server,
                            Uri.EscapeDataString(userName),
                            code);
        }

        protected internal abstract string Get(Board gameBoard);
        
        /// <summary>
        /// Starts client shutdown.
        /// </summary>
        public void InitiateExit()
        {
            ShouldExit = true;
        }
    }
}
