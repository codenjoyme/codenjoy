using System;
using System.Linq;
using System.Threading;
using System.Web;
using WebSocketSharp;

namespace ICanCode.Api
{
    public abstract class AbstractSolver
    {
        private const string ResponsePrefix = "board=";

        /// <summary>
        /// constructor
        /// </summary>
        /// <param name="server">server http address including email and code</param>
        public AbstractSolver(string server)
        {
            // Console.OutputEncoding = Encoding.UTF8;
            ServerUrl = server;
        }

        public string ServerUrl { get; private set; }


        /// <summary>
        /// Set this property to true to finish playing
        /// </summary>
        public bool ShouldExit { get; protected set; }

        public void Play()
        {
            string url = GetWebSocketUrl(this.ServerUrl);

            var socket = new WebSocket(url);

            socket.OnMessage += Socket_OnMessage;
            socket.Connect();

            while (!ShouldExit && socket.ReadyState != WebSocketState.Closed)
            {
                Thread.Sleep(50);
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
                    var board = new Board(boardString);

                    //Just print current state (gameBoard) to console
                    Console.Clear();
                    Console.SetCursorPosition(0, 0);
                    Console.WriteLine(board.ToString());

                    var action = WhatToDo(board).ToString();

                    Console.WriteLine("Answer: " + action);
                    Console.SetCursorPosition(0, 0);

                    ((WebSocket)sender).Send(action);
                }
            }
        }

        public static string GetWebSocketUrl(string serverUrl)
        {
            Uri uri = new Uri(serverUrl);

            var server = $"{uri.Host}:{uri.Port}";
            var userName = uri.Segments.Last();
            var code = HttpUtility.ParseQueryString(uri.Query).Get("code");

            return GetWebSocketUrl(userName, code, server);
        }

        private static string GetWebSocketUrl(string userName, string code, string server)
        {
            return string.Format("ws://{0}/codenjoy-contest/ws?user={1}&code={2}",
                            server,
                            Uri.EscapeDataString(userName),
                            code);
        }

        public abstract Command WhatToDo(Board board);

        /// <summary>
        /// Starts client shutdown.
        /// </summary>
        public void InitiateExit()
        {
            ShouldExit = true;
        }
    }
}
