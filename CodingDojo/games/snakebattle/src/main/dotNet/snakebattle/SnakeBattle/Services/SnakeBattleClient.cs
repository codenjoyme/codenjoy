/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
using System.Linq;
using System.Threading;
using Microsoft.Extensions.Logging;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Interfaces.Utilities;
using SnakeBattle.Models;
using WebSocket4Net;

namespace SnakeBattle.Services
{
    public class SnakeBattleClient : ISnakeBattleClient
    {
        private const string ResponsePrefix = "board=";
        private readonly IBoardStringParser _boardStringParser;
        private readonly IDisplayService _displayService;
        private readonly GameConfiguration _gameConfiguration;
        private readonly ILogger<SnakeBattleClient> _logger;
        private readonly ISolver _solver;
        private WebSocket _webSocket;

        public SnakeBattleClient(
            ISolver solver,
            IDisplayService displayService,
            IBoardStringParser boardStringParser,
            ILogger<SnakeBattleClient> logger,
            GameConfiguration gameConfiguration
        )
        {
            _solver = solver;
            _displayService = displayService;
            _boardStringParser = boardStringParser;
            _logger = logger;
            _gameConfiguration = gameConfiguration;
        }

        public void Connect()
        {
            var webSocketUrl = MakeWebSocketUrl(_gameConfiguration.ServerUrl);

            _logger.LogInformation($"Connecting to web socket: {webSocketUrl}");
            
            _webSocket = new WebSocket(webSocketUrl)
            {
                AutoSendPingInterval = 30,
                EnableAutoSendPing = true
            };

            _webSocket.MessageReceived += WebSocketOnMessageReceived;
            _webSocket.Closed += WebSocketOnClosed;

            _webSocket.Open();
        }

        private static string MakeWebSocketUrl(string serverUrl)
        {
            return serverUrl
                .Replace("http", "ws")
                .Replace("board/player/", "ws?user=")
                .Replace("?code=", "&code=");
        }

        private void Send(string message)
        {
            _webSocket?.Send(message);
        }

        private void WebSocketOnClosed(object sender, EventArgs e)
        {
            _logger.LogWarning("Client disconnected.");
            _displayService.ShowError("Client disconnected.");
            Thread.Sleep(1000);
            _displayService.ShowError("Connecting...");
            _webSocket?.Open();
        }

        private void WebSocketOnMessageReceived(object sender, MessageReceivedEventArgs eventArgs)
        {
            var boardString = eventArgs.Message.Substring(ResponsePrefix.Length);
            var board = _boardStringParser.Parse(boardString);

            var messageToSend = string.Empty;

            try
            {
                var playerCommands = _solver.Decide(board);
                messageToSend = string.Join(",", playerCommands.Select(command => command.ToString()));

                _displayService.RenderBoard(boardString, messageToSend);
            }
            catch (Exception exception)
            {
                _logger.LogError($"Error solving round: {exception}");
                _displayService.ShowError(exception);

                if (!_gameConfiguration.IgnoreRoundExceptions)
                {
                    throw;
                }
            }

            Send(messageToSend);
        }
    }
}