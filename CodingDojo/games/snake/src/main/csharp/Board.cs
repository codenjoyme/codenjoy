using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SnakeClient
{
    internal class Board
    {
        public string RawBoard { get; private set; }

        public int MapSize { get; private set; }

        public void Parse(string input)
        {
            if (input.StartsWith("board="))
                input = input.Substring(6);

            RawBoard = input
                .Replace('☼', '#')  // wall
                .Replace('▲', '0').Replace('◄', '0').Replace('►', '0').Replace('▼', '0')  // head
                .Replace('║', 'o').Replace('═', 'o').Replace('╙', 'o').Replace('╘', 'o')  // body
                .Replace('╓', 'o').Replace('╕', 'o')
                .Replace('╗', 'o').Replace('╝', 'o').Replace('╔', 'o').Replace('╚', 'o')  // body
                .Replace('☻', 'X')  // bad apple
                .Replace('☺', '$'); // good apple
            int length = RawBoard.Length;
            MapSize = (int) Math.Sqrt(length);
        }

        public char GetAt(int x, int y)
        {
            return RawBoard[x + y * MapSize];
        }

        public string GetDisplay()
        {
            StringBuilder sb = new StringBuilder();

            for (int line = 0; line < MapSize; line++)
            {
                if (line > 0)
                    sb.AppendLine();
                sb.Append("  ");
                sb.Append(RawBoard.Substring(MapSize * line, MapSize));
            }

            return sb.ToString();
        }
    }
}
