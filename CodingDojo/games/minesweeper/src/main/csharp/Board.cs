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
using System.Text;

namespace MinesweeperClient
{
	public class Board
	{
		private string RawBoard;
		private LengthToXY LengthXY;

		/// <summary>
		/// GameBoard size (actual board size is MapSize x MapSize cells)
		/// </summary>
		public int Size { get; private set; }

		public Board(String boardString)
		{
			RawBoard = boardString.Replace("\n", "");
			Size = (int)Math.Sqrt(RawBoard.Length);
			LengthXY = new LengthToXY(Size);
		}

		public List<Point> Get(Element element)
		{
			List<Point> result = new List<Point>();

			for (int i = 0; i < Size * Size; i++)
			{
				Point pt = LengthXY.GetXY(i);

				if ((Element)RawBoard[LengthXY.GetLength(pt.X, pt.Y)] == element)
				{
					result.Add(pt);
				}
			}

			return result;
		}

        public List<PointExtendView> GetAllExtended()
        {
            var result = new List<PointExtendView>(Size * Size);
            for (int x = 0; x < Size; x++)
                for (int y = 0; y < Size; y++)
                    result.Add(new PointExtendView(x, y, GetAtInternal(x, y)));
            return result;
        }

		public bool IsAt(Point point, Element element)
		{
			if (point.IsOutOf(Size))
			{
				return false;
			}
			return GetAt(point) == element;
		}

		public Element GetAt(Point point)
		{
			if (point.IsOutOf(Size))
			{
				return Element.NONE;
			}
			return  GetAtInternal(point.X, point.Y);
		}

        public Element GetAt(int x, int y)
		{
			if (x < 0 || x >= Size || y < 0 || y >= Size)
				throw new Exception("Out of range");
            return GetAtInternal(x, y);
        }

        public List<Point> GetBarriers()
		{
			return Get(Element.BORDER);
		}

		public List<Point> GetWalls()
		{
			return Get(Element.BORDER);
		}

		public bool IsAt(int x, int y, Element element)
		{
			var point = new Point(x, y);
			if (point.IsOutOf(Size))
			{
				return false;
			}
			return GetAt(point) == element;
		}

		public bool IsNear(int x, int y, Element element)
		{
			return CountNear(x, y, element) > 0;
		}

		public bool IsBarrierAt(int x, int y)
		{
			return IsAt(x, y, Element.BORDER);
		}

		public int CountNear(int x, int y, Element element)
		{
			int count = 0;
			for (int i = x - 1; i < x + 2; i++)
			{
				for (int j = y - 1; j < y + 2; j++)
				{
					if (i == x && j == y)
						continue;
					if (IsAt(i, j, element))
						count++;
				}
			}

			return count;
		}

		public Point GetMe()
		{
			var points = Get(Element.HERO);
			if (points.Count > 0)
			{
				return points[0];
			}

			return new Point();
		}

		public bool IsGameOver()
		{
			return Get(Element.HIDDEN).Count == 0;
		}

		public Element ValueOf(char ch)
		{
			return (Element)ch;
		}

		public List<Element> GetNear(int x, int y)
		{
			List<Element> elements = new List<Element>();
			for (int i = x - 1; i < x + 2; i++)
			{
				for (int j = y - 1; j < y + 2; j++)
				{
					if (i == x && j == y)
						continue;
					elements.Add(GetAt(i, j));
				}
			}

			return elements;
		}

		public List<Element> GetNear(Point point)
		{
			return GetNear(point.X, point.Y);
		}

		public bool IsOutOfField(int x, int y)
		{
			var point = new Point(x, y);
			return point.IsOutOf(Size);
		}

		public bool IsOutOfField(Point point)
		{
			return point.IsOutOf(Size);
		}

		public override string ToString()
		{
			StringBuilder sb = new StringBuilder();

			for (int line = 0; line < Size; line++)
			{
				if (line > 0)
					sb.AppendLine();
				sb.Append("  ");
				sb.Append(RawBoard.Substring(Size * line, Size));
			}

			return sb.ToString();
		}

        private Element GetAtInternal(int x, int y)
        {
            return (Element)RawBoard[LengthXY.GetLength(x, y)];
        }
    }
}
