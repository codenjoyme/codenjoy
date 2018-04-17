using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SnakeClient
{
    internal class MySnakeBot
    {
        public string HeadlineText { get; private set; }
        public string DisplayText { get; private set; }
        public string CommandText { get; private set; }

        public string Process(string input)
        {
            HeadlineText = "";
            DisplayText = "";

            Board board = new Board();
            board.Parse(input);
            DisplayText = board.GetDisplay();

            Random random = new Random((int)DateTime.Now.Ticks);
            int move = random.Next(0, 3);
            switch (move)
            {
                case 0:
                    CommandText = "UP";
                    break;
                case 1:
                    CommandText = "DOWN";
                    break;
                case 2:
                    CommandText = "LEFT";
                    break;
                case 3:
                    CommandText = "RIGHT";
                    break;
            }

            return CommandText;
        }
    }
}
