/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

namespace LemonadeClient
{
    public class ClientWrapper
    {
        private WebSocket client = null;
        private MyLemonadeBot bot = null;

        public ClientWrapper(string url, MyLemonadeBot bot)
        {
            client = new WebSocket(url);

            client.OnOpen += Client_OnOpen;
            client.OnClose += Client_OnClose;
            client.OnMessage += Client_OnMessage;
            client.OnError += Client_OnError;

            this.bot = bot;

            client.Connect();
        }

        private void Client_OnError(object sender, ErrorEventArgs args)
        {
            LogMessage($"WebSocket Error: {args.Exception}");
        }

        private void Client_OnMessage(object sender, MessageEventArgs args)
        {
            if (!args.IsText)
                return;

            bot.Received(args.Data);

            LogMessage($"Messages: {bot.Messages}");
            string command = bot.Process();
            LogMessage($"Answer: {command}");
            client.Send(command);
        }

        private void Client_OnClose(object sender, CloseEventArgs closeEventArgs)
        {
            LogMessage("WebSocket Closed");
        }

        private void Client_OnOpen(object sender, EventArgs eventArgs)
        {
            LogMessage("WebSocket Opened");
        }

        private void LogMessage(string text)
        {
            Console.WriteLine(text);
        }
    }
}
