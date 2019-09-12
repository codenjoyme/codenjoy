using System;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using BikeClient.GameArea;

namespace BikeClient
{
    internal class Program
    {
        private const int ReceiveChunkSize = 1024 * 10;
        private const bool Verbose = true;
        private const string GameServerUri = "http://10.6.193.111:8080/codenjoy-contest/board/player/ntgk9j9kf544p5tuiapm?code=1972210637167945904";
        private static readonly object ConsoleLock = new object();
        private static readonly Encoding Encoder = new UTF8Encoding(false);
        private static readonly AiSolver PlayGround = new AiSolver();

        private static void Main(
            string[] args)
        {
            Thread.Sleep(1000);
            var uri = GameServerUri.Replace("http", "ws");
            uri = uri.Replace("board/player/", "ws?user=");
            uri = uri.Replace("?code", "&code");
            Connect(uri).GetAwaiter().GetResult();
        }

        private static async Task Connect(
            string uri)
        {
            ClientWebSocket webSocket = null;
            try
            {
                webSocket = new ClientWebSocket();
                await webSocket.ConnectAsync(new Uri(uri), CancellationToken.None);
                await Receive(webSocket);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: {0}", ex);
            }
            finally
            {
                if (webSocket != null)
                {
                    webSocket.Dispose();
                }

                Console.WriteLine();

                lock (ConsoleLock)
                {
                    Console.ForegroundColor = ConsoleColor.Red;
                    Console.WriteLine("WebSocket closed.");
                    Console.ResetColor();
                }
            }
        }

        private static async Task Receive(
            ClientWebSocket webSocket)
        {
            var buffer = new byte[ReceiveChunkSize];
            while (webSocket.State == WebSocketState.Open)
            {
                var result = await webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
                if (result.MessageType == WebSocketMessageType.Close)
                {
                    await webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, string.Empty, CancellationToken.None);
                } else
                {
                    for (var i = result.Count;
                        i < buffer.Length;
                        i++) buffer[i] = 0;
                    var message = Encoder.GetString(buffer, 0, result.Count);
                    LogStatus(true);
                    PlayGround.ForString(message);
                    var command = PlayGround.Play();
                    await Send(webSocket, command);
                }
            }
        }

        private static async Task Send(
            ClientWebSocket webSocket,
            string command)
        {
            var buffer = Encoder.GetBytes(command);
            await webSocket.SendAsync(new ArraySegment<byte>(buffer), WebSocketMessageType.Text, true, CancellationToken.None);
            LogStatus(false);
        }

        private static void LogStatus(
            bool receiving)
        {
            lock (ConsoleLock)
            {
                if (Verbose && !receiving)
                {
                    Console.WriteLine(DateTime.Now.ToString());
                    Console.Write("  ");
                }

                Console.ResetColor();
            }
        }
    }
}
