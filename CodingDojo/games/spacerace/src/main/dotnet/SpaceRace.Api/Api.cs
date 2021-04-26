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
using System.Threading;
using System.Threading.Tasks;
using SpaceRace.Api.Interfaces;
using WebSocketSharp;

namespace SpaceRace.Api
{
    public class Api : IDisposable
    {
        private const string BoardPrefix = "board=";
        private readonly ISolver _solver;
        private readonly IApiLogger _logger;
        private readonly int _reconnectionIntervalMs = 1000;
        private int _tryCount = 0;
        private readonly WebSocket _socket;
        private readonly CancellationTokenSource _cts;
        private bool _disposedValue;

        public Api(string url, int reconnectionInterval, ISolver solver, IApiLogger logger)
        {
            _reconnectionIntervalMs = reconnectionInterval;
            _solver = solver;
            _logger = logger;
            var _server = url.Replace("http", "ws").Replace("board/player/", "ws?user=").Replace("?code=", "&code=");
            _cts = new CancellationTokenSource();
            _socket = new WebSocket(_server); 
            // _socket.SslConfiguration.EnabledSslProtocols = System.Security.Authentication.SslProtocols.Tls12;
            // _socket.SslConfiguration.EnabledSslProtocols = System.Security.Authentication.SslProtocols.Default;
            _socket.OnMessage += Socket_OnMessage;

            _ = ConnectWithReconnectionAsync(_cts.Token);
        }

        public void Stop()
        {
            _cts.Cancel();
        }
        
        /// <summary>
        /// Starts connecting to server with retries.
        /// On success connection continues to check connection status.
        /// </summary>
        /// <param name="ct"></param>
        /// <returns></returns>
        private async Task ConnectWithReconnectionAsync(CancellationToken ct)
        {
            await Task.Run(async () =>
            {
                while (!ct.IsCancellationRequested)
                {
                    if (_socket.ReadyState != WebSocketState.Open)
                    {
                        Connect();
                    }

                    await Task.Delay(_reconnectionIntervalMs, ct);
                }
            });
        }

        private void Connect()
        {
            string connectingMessage = _tryCount == 0
                ? "Connecting..."
                : $"Trying to reconnect... ({_tryCount})";
            _logger.Log(connectingMessage);
            _tryCount++;

            try
            {
                _socket.Connect();

                // reset try count on success connection
                if (_socket.ReadyState == WebSocketState.Open)
                {
                    _tryCount = 0;
                }
            }
            catch (Exception ex)
            {
                _logger.Log($"Can't connect to server: {ex.Message}");
            }
        }

        private void Socket_OnMessage(object sender, MessageEventArgs e)
        {
            if (!_cts.IsCancellationRequested)
            {
                var response = e.Data;

                if (!response.StartsWith(BoardPrefix))
                {
                   _logger.Log("Something strange is happening on the server... Response:\n{0}", response);
                    _cts.Cancel();
                }
                else
                {
                    var boardString = response.Substring(BoardPrefix.Length);

                    var board = new Board(boardString);
                    _logger.LogBoard(board);
                    var command = _solver.Get(board);
                    _logger.LogResult(command);
                    ((WebSocket)sender).Send(command.ToString());
                }
            }
        }

        protected virtual void Dispose(bool disposing)
        {
            if (!_disposedValue)
            {
                if (disposing)
                {
                    _cts?.Cancel();
                    _cts?.Dispose();
                    if (_socket != null)
                    {
                        _socket.OnMessage -= Socket_OnMessage;
                        _socket.Close();
                    }
                }

                _disposedValue = true;
            }
        }

        void IDisposable.Dispose()
        {
            Dispose(disposing: true);
        }
    }
}
