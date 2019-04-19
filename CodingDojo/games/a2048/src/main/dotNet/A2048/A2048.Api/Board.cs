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

namespace A2048.Api
{
    public class Board
    {

        private String BoardString { get; }
        private LengthToXY LengthXY;

        public Board(String boardString)
        {
            BoardString = boardString.Replace("\n", "");
            LengthXY = new LengthToXY(Size);
        }        

        /// <summary>
        /// GameBoard size (actual board size is Size x Size cells)
        /// </summary>
        public int Size
        {
            get
            {
                return (int)Math.Sqrt(BoardString.Length);
            }
        }

        public List<Point> GetBarriers()
        {
            return Get(Element._x);
        }

        public Element GetAt(Point point)
        {
            if (point.IsOutOf(Size))
            {
                return Element.NONE;
            }
            return (Element)BoardString[LengthXY.GetLength(point.X, point.Y)];
        }

        public bool IsAt(Point point, Element element)
        {
            if (point.IsOutOf(Size))
            {
                return false;
            }

            return GetAt(point) == element;
        }

        public string BoardAsString()
        {
            string result = "";
            for (int i = 0; i < Size; i++)
            {
                result += BoardString.Substring(i * Size, Size);
                result += "\n";
            }
            return result;
        }

        /// <summary>
        /// gets board view as string
        /// </summary>
        public string ToString()
        {
           return string.Format("{0}\n" +
                    "Barriers at: {1}",
                    BoardAsString(),
                    ListToString(GetBarriers()));
        }

        private string ListToString(List<Point> list)
        {
            return string.Join(",", list.ToArray());
        }

        public List<Point> Get(Element element)
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

        public bool IsAnyOfAt(Point point, params Element[] elements)
        {
            return elements.Any(elem => IsAt(point, elem));
        }

        public bool IsNear(Point point, Element element)
        {
            if (point.IsOutOf(Size))
                return false;

            return IsAt(point.ShiftLeft(),   element) ||
                   IsAt(point.ShiftRight(),  element) ||
                   IsAt(point.ShiftTop(),    element) ||
                   IsAt(point.ShiftBottom(), element);
        }

        public bool IsBarrierAt(Point point)
        {
            return GetBarriers().Contains(point);
        }

        public int CountNear(Point point, Element element)
        {
            if (point.IsOutOf(Size))
                return 0;

            int count = 0;
            if (IsAt(point.ShiftLeft(),   element)) count++;
            if (IsAt(point.ShiftRight(),  element)) count++;
            if (IsAt(point.ShiftTop(),    element)) count++;
            if (IsAt(point.ShiftBottom(), element)) count++;
            return count;
        }
    }
}
