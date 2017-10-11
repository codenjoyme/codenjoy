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
    public class GameBoard
    {
        public GameBoard(String boardString)
        {
            BoardString = boardString.Replace("\n", "");
        }

        public String BoardString { get; private set; }

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

        public BoardPoint GetBomberman()
        {
            return FindAllElements(BoardElement.Bomberman)
                    .Concat(FindAllElements(BoardElement.BombBomberman))
                    .Concat(FindAllElements(BoardElement.DeadBomberman))
                    .Single();
        }

        public List<BoardPoint> GetOtherBombermans()
        {
            return FindAllElements(BoardElement.OtherBomberman)
                .Concat(FindAllElements(BoardElement.OtherBombBomberman))
                .Concat(FindAllElements(BoardElement.OtherDeadBomberman))
                .ToList();
        }

        public bool isMyBombermanDead
        {
            get
            {
                return BoardString.Contains((char)BoardElement.DeadBomberman);
            }
        }

        public BoardElement GetAt(int x, int y)
        {
            if (Pt(x, y).IsOutOf(Size))
            {
                return BoardElement.Wall;
            }
            return (BoardElement)BoardString[GetShiftByPoint(x, y)];
        }

        public bool IsAt(int x, int y, BoardElement element)
        {
            var point = new BoardPoint(x, y);
            return IsAt(point, element);
        }

        public bool IsAt(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOf(Size))
            {
                return false;
            }

            return GetAt(point.X, point.Y) == element;
        }

        /// <summary>
        /// Writes board view to the console window
        /// </summary>
        public void PrintBoard()
        {
            Console.Clear();
            for (int i = 0; i < Size; i++)
            {
                Console.WriteLine(BoardString.Substring(i * Size, Size));
            }

            var data = string.Format("Bomberman at: {0}\n" +
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
            Console.WriteLine(data);
        }

        private string ListToString(List<BoardPoint> list)
        {
            return string.Join(",", list.ToArray());
        }

        private string PrintArray(object[] array)
        {
            return string.Join(",", array); ;
        }

        public List<BoardPoint> GetBarrier()
        {
            return GetMeatChoppers()
                .Concat(GetWalls())
                .Concat(GetBombs())
                .Concat(GetDestroyWalls())
                .Concat(GetOtherBombermans())
                .Distinct()
                .ToList();
        }

        public List<BoardPoint> GetMeatChoppers()
        {
            return FindAllElements(BoardElement.MeatChopper);
        }

        public List<BoardPoint> FindAllElements(BoardElement element)
        {
            List<BoardPoint> result = new List<BoardPoint>();

            for (int i = 0; i < Size * Size; i++)
            {
                BoardPoint pt = GetPointByShift(i);

                if (IsAt(pt, element))
                {
                    result.Add(pt);
                }
            }

            return result;
        }

        public List<BoardPoint> GetWalls()
        {
            return FindAllElements(BoardElement.Wall);
        }

        public List<BoardPoint> GetDestroyWalls()
        {
            return FindAllElements(BoardElement.WallDestroyable);
        }

        public List<BoardPoint> GetBombs()
        {
            return FindAllElements(BoardElement.BombTimer1)
                .Concat(FindAllElements(BoardElement.BombTimer2))
                .Concat(FindAllElements(BoardElement.BombTimer3))
                .Concat(FindAllElements(BoardElement.BombTimer4))
                .Concat(FindAllElements(BoardElement.BombTimer5))
                .Concat(FindAllElements(BoardElement.BombBomberman))
                .ToList();
        }

        public List<BoardPoint> GetBlasts()
        {
            return FindAllElements(BoardElement.Boom);
        }

        public List<BoardPoint> GetFutureBlasts()
        {
            var bombs = GetBombs()
                .Concat(FindAllElements(BoardElement.OtherBombBomberman))
                .Concat(FindAllElements(BoardElement.BombBomberman));

            var result = new List<BoardPoint>();

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

        public bool HasElementAt(BoardPoint point, params BoardElement[] elements)
        {
            return elements.Any(elem => IsAt(point, elem));
        }

        private static BoardPoint Pt(int x, int y)
        {
            return new BoardPoint(x, y);
        }

        public bool IsNearToElement(int x, int y, BoardElement element)
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

        public int GetCountElementsNearToPoint(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOf(Size))
                return 0;

            //GetHashCode() in classic MS.NET for bool returns 1 for true and 0 for false;
            return IsAt(point.ShiftLeft(), element).GetHashCode() +
                   IsAt(point.ShiftRight(), element).GetHashCode() +
                   IsAt(point.ShiftTop(), element).GetHashCode() +
                   IsAt(point.ShiftBottom(), element).GetHashCode();
        }

        private int GetShiftByPoint(int x, int y)
        {
            return x * Size + y;
        }

        private BoardPoint GetPointByShift(int shift)
        {
            return new BoardPoint(shift % Size, shift / Size);
        }
    }
}