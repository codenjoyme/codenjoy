using System;
using System.Text;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Models;

namespace SnakeBattle.Services
{
    public class DisplayService : IDisplayService
    {
        private readonly GameConfiguration _gameConfiguration;

        private int _tick;

        public DisplayService(GameConfiguration gameConfiguration)
        {
            _gameConfiguration = gameConfiguration;
            Console.OutputEncoding = Encoding.UTF8;
        }

        public void RenderBoard(string board, string botCommand)
        {
            PrepareConsole();
            Console.WriteLine(FormatBoardString(board));

            Console.WriteLine($"Decision: {botCommand}.");
            Console.WriteLine("Press 'X' to exit.");
        }

        private void PrepareConsole()
        {
            if (_gameConfiguration.KeepConsoleHistory)
            {
                Console.WriteLine();
                Console.WriteLine($"============  {_tick++}  ============");
                Console.WriteLine();
                return;
            }

            Console.Clear();
            Console.SetCursorPosition(0, 0);
        }

        public void ShowError(string message)
        {
            PrepareConsole();
            Console.WriteLine($"Error: {message}.");
            Console.WriteLine("Press 'X' to exit.");
            Console.SetCursorPosition(0, 0);
        }

        public void ShowError(Exception message)
        {
            PrepareConsole();

            Console.WriteLine($"Exception: {message.Message}.");
            Console.WriteLine($"StackTrace: {message.StackTrace}.");
            Console.WriteLine("Press 'X' to exit.");
            Console.SetCursorPosition(0, 0);
        }

        private static string FormatBoardString(string board)
        {
            var boardSize = (int) Math.Sqrt(board.Length);

            var result = string.Empty;

            for (var i = 0; i < boardSize; i++)
            {
                result += board.Substring(i * boardSize, boardSize);
                result += '\n';
            }

            return result;
        }
    }
}