using System;
using System.Text;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle.Services
{
    public class DisplayService : IDisplayService
    {
        public DisplayService()
        {
            Console.OutputEncoding = Encoding.UTF8;
        }

        public void RenderBoard(string board, string botCommand)
        {
            Console.Clear();
            Console.SetCursorPosition(0, 0);
            Console.WriteLine(FormatBoardString(board));

            Console.WriteLine($"Decision: {botCommand}.");
            Console.WriteLine("Press 'X' to exit.");
            Console.SetCursorPosition(0, 0);
        }

        public void ShowError(string message)
        {
            Console.Clear();
            Console.SetCursorPosition(0, 0);

            Console.WriteLine($"Error: {message}.");
            Console.WriteLine("Press 'X' to exit.");
            Console.SetCursorPosition(0, 0);
        }

        public void ShowError(Exception message)
        {
            Console.Clear();
            Console.SetCursorPosition(0, 0);

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