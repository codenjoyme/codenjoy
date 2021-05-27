using System;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using SnakeBattle.Exceptions;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Interfaces.Utilities;

namespace SnakeBattle.Services
{
    public class SnakeBattleClient : IDisposable, ISnakeBattleClient
    {
        private const string ResponsePrefix = "board=";
        private readonly IBoardStringParser _boardStringParser;
        private readonly ClientWebSocket _clientWebSocket;
        private readonly IDisplayService _displayService;
        private readonly ISolver _solver;

        public SnakeBattleClient(
            ISolver solver,
            IDisplayService displayService,
            IBoardStringParser boardStringParser
        )
        {
            _solver = solver;
            _displayService = displayService;
            _boardStringParser = boardStringParser;

            _clientWebSocket = new ClientWebSocket();
        }

        public void Dispose()
        {
            _clientWebSocket.Dispose();
        }

        public async Task ConnectAsync(string serverUrl)
        {
            var webSocketUrl = MakeWebSocketUrl(serverUrl);
            await _clientWebSocket.ConnectAsync(new Uri(webSocketUrl), CancellationToken.None);
            await ListenAsync();
        }

        private async Task HandleMessageAsync(string message)
        {
            var boardString = message[ResponsePrefix.Length..];
            var board = _boardStringParser.Parse(boardString);
            var botCommand = _solver.Decide(board).ToString().ToUpper();

            _displayService.Render(boardString, botCommand);

            await SendAsync(botCommand);
        }

        private static bool IsMessageValid(string response)
        {
            return response.StartsWith(ResponsePrefix);
        }

        private async Task ListenAsync()
        {
            var buffer = new ArraySegment<byte>(new byte[10240]);
            while (true)
            {
                var result = await _clientWebSocket.ReceiveAsync(buffer, CancellationToken.None);
                if (result.MessageType == WebSocketMessageType.Close)
                {
                    break;
                }

                var message = Encoding.UTF8.GetString(buffer.Array, 0, result.Count);
                if (!IsMessageValid(message))
                {
                    throw new SnakeBattleClientException($"Server send invalid message: {message}");
                }

                await HandleMessageAsync(message);
            }
        }

        private static string MakeWebSocketUrl(string serverUrl)
        {
            return serverUrl
                .Replace("http", "ws")
                .Replace("board/player/", "ws?user=")
                .Replace("?code=", "&code=");
        }

        private async Task SendAsync(string str)
        {
            var bytesToSend = new ArraySegment<byte>(Encoding.UTF8.GetBytes(str));
            await _clientWebSocket.SendAsync(bytesToSend, WebSocketMessageType.Text, true, CancellationToken.None);
        }
    }
}