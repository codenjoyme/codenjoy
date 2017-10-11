/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
using System.Threading;
using System.Net.WebSockets;
using System.Threading.Tasks;
using System.Text;
using Bomberman.Api;

namespace Demo
{
    class WSTest
    {
        static void Main(string[] args)
        {
            //Task t = Connect();
            //t.Wait();

            Uri wsUri = new Uri(string.Format(
                    "ws://ecsc00104eef.epam.com:8080/codenjoy-contest/ws?user={0}",
                    Uri.EscapeDataString("oleksandr_baglai@epam.com")));
            WebSocket2 socket = new WebSocket2(wsUri);
            socket.Connect();
            while (true)
            {
                socket.Send("ACT");
                Console.WriteLine(socket.Recv());
            }
        }

        /// Thanks to https://www.codeproject.com/Articles/618032/Using-WebSocket-in-NET-Part
        private static async Task Connect()
        {
            using (ClientWebSocket ws = new ClientWebSocket())
            {
                string wsUri = string.Format(
                    "ws://ecsc00104eef.epam.com:8080/codenjoy-contest/ws?user={0}", 
                    Uri.EscapeDataString("oleksandr_baglai@epam.com"));
                Uri serverUri = new Uri(wsUri);
                await ws.ConnectAsync(serverUri, CancellationToken.None);
                while (true)
                {
                    string msg = "ACT";
                    ArraySegment<byte> bytesToSend = new ArraySegment<byte>(
                        Encoding.UTF8.GetBytes(msg));
                    await ws.SendAsync(
                        bytesToSend, WebSocketMessageType.Text,
                        true, CancellationToken.None);
                    ArraySegment<byte> bytesReceived = new ArraySegment<byte>(new byte[1024]);
                    WebSocketReceiveResult result = await ws.ReceiveAsync(
                        bytesReceived, CancellationToken.None);
                    Console.WriteLine(Encoding.UTF8.GetString(
                        bytesReceived.Array, 0, result.Count));
                    if (ws.State != WebSocketState.Open)
                    {
                        break;
                    }
                }
            }

           
        }
    }
}
