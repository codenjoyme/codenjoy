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
			LogMessage($"WebSocket Error: {args.Exception}\r\n");
		}

		private void Client_OnMessage(object sender, MessageEventArgs args)
		{
			if (!args.IsText)
				return;

			bot.Received(args.Data);

			LogMessage($"Current state: {bot.CurrentState}\r\n");
			string command = bot.Process();
			LogMessage($"Sending: {command}\r\n");
			client.Send(command);
		}

		private void Client_OnClose(object sender, CloseEventArgs closeEventArgs)
		{
			LogMessage("WebSocket Closed\r\n");
		}

		private void Client_OnOpen(object sender, EventArgs eventArgs)
		{
			LogMessage("WebSocket Opened\r\n");
		}

		private void LogMessage(string text)
		{
			Console.WriteLine(text);
		}
	}
}