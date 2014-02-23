using System;

namespace Bomberman.Api
{
    public abstract class BombermanBase
    {
        protected readonly string Server = @"ws://tetrisj.jvmhost.net:12270/codenjoy-contest/ws";
        private const string ResponsePrefix = "board=";

        public BombermanBase(string userName)
        {
            UserName = userName;
        }

        public string UserName { get; private set; }

        /// <summary>
        /// Set this property to true to finish playing
        /// </summary>
        public bool ShouldExit { get; protected set; }

        public void Play()
        {
            var uri = new Uri(Server + "?user=" + Uri.EscapeDataString(UserName));

            using (var socket = new WebSocket(uri))
            {
                socket.Connect();

                while (!ShouldExit)
                {
                    var response = socket.Recv();

                    if (!response.StartsWith(ResponsePrefix))
                    {
                        Console.WriteLine("Something strange is happening on the server... Response:\n{0}", response);
                        ShouldExit = true;
                    }
                    else
                    {
                        var boardString = response.Substring(ResponsePrefix.Length);

                        var action = DoMove(new GameBoard(boardString));

                        socket.Send(BombermanActionToString(action));
                    }
                }
            }
        }

        protected abstract BombermanAction DoMove(GameBoard gameBoard);

        private static string BombermanActionToString(BombermanAction action)
        {
            switch (action)
            {
                case BombermanAction.GoLeft: return "left";
                case BombermanAction.GoRight: return "right";
                case BombermanAction.GoUp: return "up";
                case BombermanAction.GoDown: return "down";
                case BombermanAction.PlaceBomb: return "act";
                default: return "stop";
            }
        }
    }
}