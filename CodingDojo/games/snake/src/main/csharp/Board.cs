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
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SnakeClient
{
    public class Board
    {
        private string RawBoard;
        private LengthToXY LengthXY;

        public int MapSize { get; private set; }

        /// <summary>
        /// GameBoard size (actual board size is Size x Size cells)
        /// </summary>
        public int Size
        {
            get
            {
                return (int)Math.Sqrt(RawBoard.Length);
            }
        }

        public Board(String boardString)
        {
            RawBoard = boardString.Replace("\n", "");
            LengthXY = new LengthToXY(Size);
        }

        public Point GetHead()
        {
            return Get(Element.HEAD_UP)
                .Concat(Get(Element.HEAD_DOWN))
                .Concat(Get(Element.HEAD_LEFT))
                .Concat(Get(Element.HEAD_RIGHT))
                .SingleOrDefault();
        }

        public List<Point> GetApples()
        {
            return Get(Element.GOOD_APPLE);
        }

        public List<Point> GetStones()
        {
            return Get(Element.BAD_APPLE);
        }

        public List<Point> GetWalls()
        {
            return Get(Element.BREAK);
        }

        public List<Point> GetSnake()
        {
            List<Point> snake = new List<Point>();
            Point head = GetHead();
            if (head == null)
            {
                return snake;
            }

            snake.Add(head);
            return snake
                .Concat(Get(Element.TAIL_END_DOWN))
                .Concat(Get(Element.TAIL_END_LEFT))
                .Concat(Get(Element.TAIL_END_UP))
                .Concat(Get(Element.TAIL_END_RIGHT))
                .Concat(Get(Element.TAIL_HORIZONTAL))
                .Concat(Get(Element.TAIL_VERTICAL))
                .Concat(Get(Element.TAIL_LEFT_DOWN))
                .Concat(Get(Element.TAIL_LEFT_UP))
                .Concat(Get(Element.TAIL_RIGHT_DOWN))
                .Concat(Get(Element.TAIL_RIGHT_UP))
                .ToList();
        }

        public List<Point> GetBarriers()
        {
            return GetSnake()
                .Concat(GetStones())
                .Concat(GetWalls())
                .ToList();
        }

        public bool IsBarrierAt(Point point)
        {
            return GetBarriers().Contains(point);
        }

        public bool IsSnakeAlive()
        {
            return GetHead() != null;
        }

        private void Parse(string input)
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
            MapSize = (int)Math.Sqrt(length);
        }

        private Element GetAt(Point point)
        {
            if (point.IsOutOf(Size))
            {
                return Element.NONE;
            }
            return (Element)RawBoard[LengthXY.GetLength(point.X, point.Y)];
        }

        private bool IsAt(Point point, Element element)
        {
            if (point.IsOutOf(Size))
            {
                return false;
            }
            return GetAt(point) == element;
        }

        private List<Point> Get(Element element)
        {
            List<Point> result = new List<Point>();

            for (int i = 0; i < Size * Size; i++)
            {
                Point pt = LengthXY.GetXY(i);

                if (IsAt(pt, element))
                {
                    result.Add(pt);
                }
            }

            return result;
        }

        private char GetAt(int x, int y)
        {
            return RawBoard[x + y * MapSize];
        }

        private string GetDisplay()
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
