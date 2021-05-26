using System;
using SnakeBattle.Services;
using SnakeBattle.Utilities;

namespace SnakeBattle
{
    internal class Program
    {
        private const string ServerUrl = "http://localhost:8080/codenjoy-contest/board/player/0?code=000000000000";

        private static void Main(string[] args)
        {
            Console.SetWindowSize(Console.LargestWindowWidth - 3, Console.LargestWindowHeight - 3);

            var solver = new Solver();
            var displayService = new DisplayService();
            var boardStringParser = new BoardStringParser();

            using var snakeBattleClient = new SnakeBattleClient(
                ServerUrl,
                solver,
                displayService,
                boardStringParser
            );
            
            snakeBattleClient.Connect();

            Console.ReadKey();
        }
    }
}