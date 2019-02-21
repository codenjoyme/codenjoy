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
﻿using System;
using System.Collections.Generic;
using System.Linq;

namespace Loderunner.Api
{
    public class GameBoard
    {
        public GameBoard(string boardString)
        {
            BoardString = boardString.Replace("\n", "");
        }

        public string BoardString { get; private set; }

        public int Size
        {
            get
            {
                return (int)Math.Sqrt(BoardString.Length);
            }
        }

        public BoardPoint GetMe
        {
            get
            {
                return FindAllElements(BoardElement.HeroDie)
                    .Concat(FindAllElements(BoardElement.HeroDrillLeft))
                    .Concat(FindAllElements(BoardElement.HeroDrillRight))
                    .Concat(FindAllElements(BoardElement.HeroFallRight))
                    .Concat(FindAllElements(BoardElement.HeroFallLeft))
                    .Concat(FindAllElements(BoardElement.HeroLadder))
                    .Concat(FindAllElements(BoardElement.HeroLeft))
                    .Concat(FindAllElements(BoardElement.HeroRight))
                    .Concat(FindAllElements(BoardElement.HeroPipeLeft))
                    .Concat(FindAllElements(BoardElement.HeroPipeRight))
                    .Single();
            }
        }

        public bool IsGameOver
        {
            get
            {
                return BoardString.Contains((char)BoardElement.HeroDie);
            }
        }

        public bool HasElementAt(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOfBoard(Size))
            {
                return false;
            }

            return GetElementAt(point) == element;
        }

        public BoardElement GetElementAt(BoardPoint point)
        {
            return (BoardElement)BoardString[GetShiftByPoint(point)];
        }

        public void PrintBoard()
        {
            for (int i = 0; i < Size; i++)
            {
                Console.WriteLine(BoardString.Substring(i * Size, Size));
            }
        }

        public List<BoardPoint> FindAllElements(BoardElement element)
        {
            List<BoardPoint> result = new List<BoardPoint>();

            for (int i = 0; i < Size * Size; i++)
            {
                BoardPoint pt = GetPointByShift(i);

                if (HasElementAt(pt, element))
                {
                    result.Add(pt);
                }
            }

            return result;
        }

        public List<BoardPoint> GetEnemyPositions()
        {
            return FindAllElements(BoardElement.EnemyLadder)
                .Concat(FindAllElements(BoardElement.EnemyLeft))
                .Concat(FindAllElements(BoardElement.EnemyPipeLeft))
                .Concat(FindAllElements(BoardElement.EnemyPipeRight))
                .Concat(FindAllElements(BoardElement.EnemyRight))
                .Concat(FindAllElements(BoardElement.EnemyPit))
                .ToList();
        }

        public List<BoardPoint> GetOtherHeroPositions()
        {
            return FindAllElements(BoardElement.OtherHeroLadder)
                .Concat(FindAllElements(BoardElement.OtherHeroLeft))
                .Concat(FindAllElements(BoardElement.OtherHeroPipeLeft))
                .Concat(FindAllElements(BoardElement.OtherHeroPipeRight))
                .Concat(FindAllElements(BoardElement.OtherHeroRight))
                //.Concat(FindAllElements(BoardElement.OtherHeroPit))
                .ToList();
        }

        public List<BoardPoint> GetWallPositions()
        {
            return FindAllElements(BoardElement.Brick)
            .Concat(FindAllElements(BoardElement.UndestroyableWall))
            .ToList();
        }

        public List<BoardPoint> GetLadderPositions()
        {
            return FindAllElements(BoardElement.Ladder)
            .Concat(FindAllElements(BoardElement.HeroLadder))
            .ToList();
        }

        public List<BoardPoint> GetGoldPositions()
        {
            return FindAllElements(BoardElement.Gold)
            .ToList();
        }

        public List<BoardPoint> GetPipePositions()
        {
            return FindAllElements(BoardElement.Pipe)
            .Concat(FindAllElements(BoardElement.HeroPipeLeft))
            .Concat(FindAllElements(BoardElement.HeroPipeRight))
            .Concat(FindAllElements(BoardElement.OtherHeroPipeLeft))
            .Concat(FindAllElements(BoardElement.OtherHeroPipeRight))
            .Concat(FindAllElements(BoardElement.EnemyPipeLeft))
            .Concat(FindAllElements(BoardElement.EnemyPipeRight))
            .ToList();
        }

        public bool HasElementAt(BoardPoint point, params BoardElement[] elements)
        {
            return elements.Any(elem => HasElementAt(point, elem));
        }

        public bool IsNearToElement(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOfBoard(Size))
                return false;

            return HasElementAt(point.ShiftBottom(), element)
                   || HasElementAt(point.ShiftTop(), element)
                   || HasElementAt(point.ShiftLeft(), element)
                   || HasElementAt(point.ShiftRight(), element);
        }

        public bool HasEnemyAt(BoardPoint point)
        {
            return GetEnemyPositions().Contains(point);
        }

        public bool HasOtherHeroAt(BoardPoint point)
        {
            return GetOtherHeroPositions().Contains(point);
        }

        public bool HasWallAt(BoardPoint point)
        {
            return GetWallPositions().Contains(point);
        }

        public bool HasLadderAt(BoardPoint point)
        {
            return GetLadderPositions().Contains(point);
        }

        public bool HasGoldAt(BoardPoint point)
        {
            return GetGoldPositions().Contains(point);
        }

        public bool HasPipeAt(BoardPoint point)
        {
            return GetPipePositions().Contains(point);
        }

        public int GetCountElementsNearToPoint(BoardPoint point, BoardElement element)
        {
            if (point.IsOutOfBoard(Size))
                return 0;

            //GetHashCode() in classic MS.NET for bool returns 1 for true and 0 for false;
            return HasElementAt(point.ShiftLeft(), element).GetHashCode() +
                   HasElementAt(point.ShiftRight(), element).GetHashCode() +
                   HasElementAt(point.ShiftTop(), element).GetHashCode() +
                   HasElementAt(point.ShiftBottom(), element).GetHashCode();
        }

        private int GetShiftByPoint(BoardPoint point)
        {
            return point.Y * Size + point.X;
        }

        private BoardPoint GetPointByShift(int shift)
        {
            return new BoardPoint(shift % Size, shift / Size);
        }
    }
}
