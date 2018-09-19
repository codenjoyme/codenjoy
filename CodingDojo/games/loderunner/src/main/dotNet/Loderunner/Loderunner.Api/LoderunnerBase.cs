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
using WebSocketSharp;

namespace Loderunner.Api
{
    public abstract class LoderunnerBase
    {
        protected readonly string Server = @"ws://loderunner.luxoft.com:8080/codenjoy-contest/ws";
        private const string ResponsePrefix = "board=";

        public LoderunnerBase(string userName)
        {
            UserName = userName;
        }

        public string UserName { get; private set; }

        /// <summary>
        /// Set this property to true to finish playing
        /// </summary>
        public bool ShouldExit { get; protected set; }

        public void Play()
        {
            var socket = new WebSocket(Server + "?user=" + UserName);
            socket.OnMessage += Socket_OnMessage;
            socket.Connect();

            while (!ShouldExit && socket.ReadyState != WebSocketState.Closed)
            {
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

                    var action = DoMove(new GameBoard(boardString));

                    ((WebSocket)sender).Send(action);
                }
            }
        }

        protected abstract string DoMove(GameBoard gameBoard);

        protected static string LoderunnerActionToString(LoderunnerAction action)
        {
            switch (action)
            {
                case LoderunnerAction.GoLeft: return "left";
                case LoderunnerAction.GoRight: return "right";
                case LoderunnerAction.GoUp: return "up";
                case LoderunnerAction.GoDown: return "down";
                case LoderunnerAction.DrillLeft: return "act,left";
                case LoderunnerAction.DrillRight: return "act,right";
                default: return "stop";
            }
        }
    }
}
