using System;
using System.Linq;
using System.Security.Authentication;
using System.Threading.Tasks;
using SnakeBattle.Exceptions;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Interfaces.Utilities;
using WebSocketSharp;

namespace SnakeBattle.Services
{
    public class SnakeBattleClient : IDisposable, ISnakeBattleClient
    {
        private const string ResponsePrefix = "board=";
        private const int MaxRetriesCount = 3;
        private const int RetriesTimeoutInMilliseconds = 10000;
        private readonly IBoardStringParser _boardStringParser;
        private readonly IDisplayService _displayService;
        private readonly WebSocket _gameServer;
        private readonly ISolver _solver;
        private int _retriesCount;

        public SnakeBattleClient(
            string serverUrl,
            ISolver solver,
            IDisplayService displayService,
            IBoardStringParser boardStringParser
        )
        {
            _solver = solver;
            _displayService = displayService;
            _boardStringParser = boardStringParser;

            _gameServer = new WebSocket(MakeWebSocketUrl(serverUrl))
            {
                SslConfiguration = {EnabledSslProtocols = SslProtocols.Tls12}
            };
            _gameServer.OnMessage += GameServerOnMessage;
            _gameServer.OnClose += async (_, closeEventArgs) =>
            {
                await ReconnectAsync(closeEventArgs.WasClean, closeEventArgs.Code);
            };
        }

        public void Dispose()
        {
            if (_gameServer.ReadyState == WebSocketState.Open)
            {
                _gameServer.Close();
            }

            ((IDisposable) _gameServer)?.Dispose();
        }

        public void Connect()
        {
            _gameServer.Connect();
        }

        private void GameServerOnMessage(object sender, MessageEventArgs messageEventArgs)
        {
            var message = messageEventArgs.Data;
            if (!IsMessageValid(message))
            {
                throw new SnakeBattleClientException($"Server send invalid message: {message}");
            }

            var boardString = message.Substring(ResponsePrefix.Length);
            var board = _boardStringParser.Parse(boardString);
            var botCommand = _solver.Decide(board).ToString().ToUpper();

            _displayService.Render(boardString, botCommand);

            ((WebSocket) sender).Send(botCommand);
        }

        private static bool IsAllowedToReconnect(ushort code)
        {
            return new ushort[] {1006, 1011}.Contains(code);
        }

        private static bool IsMessageValid(string response)
        {
            return response.StartsWith(ResponsePrefix);
        }

        private static string MakeWebSocketUrl(string serverUrl)
        {
            return serverUrl
                .Replace("http", "ws")
                .Replace("board/player/", "ws?user=")
                .Replace("?code=", "&code=");
        }

        private async Task ReconnectAsync(bool wasClean, ushort code)
        {
            if (!wasClean && !_gameServer.IsAlive && IsAllowedToReconnect(code))
            {
                if (_retriesCount < MaxRetriesCount)
                {
                    Console.WriteLine($"Trying to reconnect, attempt {_retriesCount + 1} of {MaxRetriesCount}...");
                    await Task.Delay(RetriesTimeoutInMilliseconds);

                    _retriesCount++;
                    _gameServer.Connect();
                }
                else
                {
                    Console.WriteLine(
                        "Could not reconnect to the server, please try again later. Press any key to exit..."
                    );
                }
            }
        }
    }
}