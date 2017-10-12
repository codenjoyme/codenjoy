/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

namespace Bomberman.Api
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

        public Point GetBomberman()
        {
            return FindAllElements(Element.Bomberman)
                    .Concat(FindAllElements(Element.BombBomberman))
                    .Concat(FindAllElements(Element.DeadBomberman))
                    .Single();
        }

        public List<Point> GetOtherBombermans()
        {
            return FindAllElements(Element.OtherBomberman)
                .Concat(FindAllElements(Element.OtherBombBomberman))
                .Concat(FindAllElements(Element.OtherDeadBomberman))
                .ToList();
        }

        public bool isMyBombermanDead
        {
            get
            {
                return BoardString.Contains((char)Element.DeadBomberman);
            }
        }

        public Element GetAt(int x, int y)
        {
            if (Pt(x, y).IsOutOf(Size))
            {
                return Element.Wall;
            }
            return (Element)BoardString[LengthXY.GetLength(x, y)];
        }

        public bool IsAt(int x, int y, Element element)
        {
            return IsAt(Pt(x, y), element);
        }

        public bool IsAt(Point point, Element element)
        {
            if (point.IsOutOf(Size))
            {
                return false;
            }

            return GetAt(point.X, point.Y) == element;
        }

        /// <summary>
        /// gets board view as string
        /// </summary>
        public string ToString()
        {
            string result = "";
            for (int i = 0; i < Size; i++)
            {
                result += BoardString.Substring(i * Size, Size) + "\n";
            }

           result += string.Format("Bomberman at: {0}\n" +
                    "Other bombermans at: {1}\n" +
                    "Meat choppers at: {2}\n" +
                    "Destroy walls at: {3}\n" +
                    "Bombs at: {4}\n" +
                    "Blasts: {5}\n" +
                    "Expected blasts at: {6}",
                    GetBomberman(),
                    ListToString(GetOtherBombermans()),
                    ListToString(GetMeatChoppers()),
                    ListToString(GetDestroyWalls()),
                    ListToString(GetBombs()),
                    ListToString(GetBlasts()),
                    ListToString(GetFutureBlasts()));
            return result;            
        }

        private string ListToString(List<Point> list)
        {
            return string.Join(",", list.ToArray());
        }

        private string PrintArray(object[] array)
        {
            return string.Join(",", array); ;
        }

        public List<Point> GetBarrier()
        {
            return GetMeatChoppers()
                .Concat(GetWalls())
                .Concat(GetBombs())
                .Concat(GetDestroyWalls())
                .Concat(GetOtherBombermans())
                .Distinct()
                .ToList();
        }

        public List<Point> GetMeatChoppers()
        {
            return FindAllElements(Element.MeatChopper);
        }

        public List<Point> FindAllElements(Element element)
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

        public List<Point> GetWalls()
        {
            return FindAllElements(Element.Wall);
        }

        public List<Point> GetDestroyWalls()
        {
            return FindAllElements(Element.WallDestroyable);
        }

        public List<Point> GetBombs()
        {
            return FindAllElements(Element.BombTimer1)
                .Concat(FindAllElements(Element.BombTimer2))
                .Concat(FindAllElements(Element.BombTimer3))
                .Concat(FindAllElements(Element.BombTimer4))
                .Concat(FindAllElements(Element.BombTimer5))
                .Concat(FindAllElements(Element.BombBomberman))
                .ToList();
        }

        public List<Point> GetBlasts()
        {
            return FindAllElements(Element.Boom);
        }

        public List<Point> GetFutureBlasts()
        {
            var bombs = GetBombs()
                .Concat(FindAllElements(Element.OtherBombBomberman))
                .Concat(FindAllElements(Element.BombBomberman));

            var result = new List<Point>();

            foreach (var bomb in bombs)
            {
                result.Add(bomb);
                result.Add(bomb.ShiftLeft());
                result.Add(bomb.ShiftRight());
                result.Add(bomb.ShiftTop());
                result.Add(bomb.ShiftBottom());
            }

            return result.Where(blast => !blast.IsOutOf(Size) && !GetWalls().Contains(blast)).Distinct().ToList();
        }

        public bool HasElementAt(Point point, params Element[] elements)
        {
            return elements.Any(elem => IsAt(point, elem));
        }

        private static Point Pt(int x, int y)
        {
            return new Point(x, y);
        }

        public bool IsNearToElement(int x, int y, Element element)
        {
            var point = Pt(x, y);
            if (point.IsOutOf(Size))
                return false;

            return IsAt(point.ShiftLeft(), element) ||
                   IsAt(point.ShiftRight(), element) ||
                   IsAt(point.ShiftTop(), element) ||
                   IsAt(point.ShiftBottom(), element);
        }

        public bool HasBarrierAt(int x, int y)
        {
            return GetBarrier().Contains(Pt(x, y));
        }

        public int GetCountElementsNearToPoint(Point point, Element element)
        {
            if (point.IsOutOf(Size))
                return 0;

            //GetHashCode() in classic MS.NET for bool returns 1 for true and 0 for false;
            return IsAt(point.ShiftLeft(), element).GetHashCode() +
                   IsAt(point.ShiftRight(), element).GetHashCode() +
                   IsAt(point.ShiftTop(), element).GetHashCode() +
                   IsAt(point.ShiftBottom(), element).GetHashCode();
        }
    }
}