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
using System.Net.WebSockets;
using System.Threading.Tasks;
using System.Threading;
using System.Text;
using ClientWebSocket = System.Net.WebSockets.Managed.ClientWebSocket;

namespace MinesweeperClient
{
	/// <summary>
	/// WebSocket implementation from 
	/// https://www.codeproject.com/Articles/618032/Using-WebSocket-in-NET-Part
	/// </summary>
	public class WebSocket : IDisposable
	{
		private Uri serverUri;
		private ClientWebSocket ws;

		public WebSocket(Uri url)
		{
			serverUri = url;

			string protocol = serverUri.Scheme;
			if (!protocol.Equals("ws") && !protocol.Equals("wss"))
				throw new ArgumentException("Unsupported protocol: " + protocol);

			ws = new ClientWebSocket();
		}

		public void Connect()
		{
			Task task = ws.ConnectAsync(serverUri, CancellationToken.None);
			task.Wait();
		}

		public void Send(string str)
		{
			ArraySegment<byte> bytesToSend = new ArraySegment<byte>(
						Encoding.UTF8.GetBytes(str));
			Task task = ws.SendAsync(
				bytesToSend, WebSocketMessageType.Text,
				true, CancellationToken.None);
			task.Wait();
		}

		public string Recv()
		{
			ArraySegment<byte> bytesReceived = new ArraySegment<byte>(new byte[10240]);
			Task<WebSocketReceiveResult> task = ws.ReceiveAsync(
				bytesReceived, CancellationToken.None);
			task.Wait();
			WebSocketReceiveResult result = task.Result;

			return Encoding.UTF8.GetString(bytesReceived.Array, 0, result.Count);
		}

		public void Close()
		{
			ws.Dispose();
		}

		public void Dispose()
		{
			Close();
		}
	}
}
