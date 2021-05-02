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

namespace SpaceRace.Api
{
    public class GameBoard
    {
        public GameBoard(string boardString)
        {
            BoardString = boardString.Replace("\n", "");
        }

        public string BoardString { get; }

        public int Size => (int)Math.Sqrt(BoardString.Length);

        /// <summary>
        /// Returns the list of points for the given element type.
        /// </summary>
        /// <param name="elements"></param>
        /// <returns></returns>
        public List<BoardPoint> FindAll(params BoardElement[] elements) =>
            EnumeratePoints(new HashSet<BoardElement>(elements))
            .ToList();

        /// <summary>
        /// Returns the point where your hero is.
        /// </summary>
        public BoardPoint GetMyPosition() => EnumeratePoints(BoardElement.Hero).Single();

        /// <summary>
        /// Returns a BoardElement object at coordinates x,y.
        /// </summary>
        /// <param name="point"></param>
        /// <returns></returns>
        public BoardElement GetAt(int x, int y) => (BoardElement)BoardString[GetShiftByPoint(x, y)];

        /// <summary>
        /// Returns True if BoardElement is at x,y coordinates.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <param name="element"></param>
        /// <returns></returns>
        public bool HasElementAt(int x, int y, BoardElement element)
        {
            if (IsOutOfBoard(x, y))
            {
                return false;
            }

            return GetAt(x, y) == element;
        }

        /// <summary>
        /// Returns true if barrier is at x,y.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool IsBarrierAt(int x, int y) => HasElementAt(x, y, BoardElement.Wall);

        /// <summary>
        /// Returns False if your hero still alive.
        /// </summary>
        /// <returns></returns>
        public bool IsHeroDead() => BoardString.Contains((char)BoardElement.DeadHero);

        /// <summary>
        /// Return the list of points for other heroes.
        /// </summary>
        /// <returns></returns>
        public List<BoardPoint> GetEnemyPositions() => EnumeratePoints(BoardElement.OtherHero).ToList();

        /// <summary>
        /// Returns the list of points for other heroes.
        /// </summary>
        /// <returns></returns>
        public List<BoardPoint> GetOtherHeroPositions() => EnumeratePoints(BoardElement.OtherHero).ToList();

        /// <summary>
        /// Returns the list of walls element Points.
        /// </summary>
        /// <returns></returns>
        public List<BoardPoint> GetWallPositions() => EnumeratePoints(BoardElement.Wall).ToList();

        /// <summary>
        /// Returns the list of barriers Points.
        /// </summary>
        /// <returns></returns>
        public List<BoardPoint> GetBarriers() => EnumeratePoints(BoardElement.Wall).ToList();

        /// <summary>
        /// Check if near exists element of chosen type.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <param name="element"></param>
        /// <returns></returns>
        public bool IsNearToElement(int x, int y, BoardElement element)
        {
            if (IsOutOfBoard(x, y))
                return false;

            return EnumerateNeighbors(new BoardPoint(x, y))
                .Any(neighbor => HasElementAt(neighbor, element));
        }

        /// <summary>
        /// Returns True if enemy exists in current point.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool HasEnemyAt(int x, int y) => HasElementAt(x, y, BoardElement.OtherHero);

        /// <summary>
        /// Returns True if other hero exists in current point.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool HasOtherHeroAt(int x, int y) => HasElementAt(x, y, BoardElement.OtherHero);

        /// <summary>
        /// Returns True if wall exists in current point.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool HasWallAt(int x, int y) => HasElementAt(x, y, BoardElement.Wall);

        /// <summary>
        /// Counts the number of occurrences of element nearby.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <param name="element"></param>
        /// <returns></returns>
        public int GetCountElementsNearToPoint(int x, int y, BoardElement element)
        {
            if (IsOutOfBoard(x, y))
                return 0;

            return EnumerateNeighbors(new BoardPoint(x, y))
                .Count(neighbor => HasElementAt(neighbor, element));
        }

        public void PrintBoard()
        {
            for (int i = 0; i < Size; i++)
            {
                Console.WriteLine(BoardString.Substring(i * Size, Size));
            }
        }

        private IEnumerable<BoardPoint> EnumerateNeighbors(BoardPoint point)
        {
            yield return point.ShiftLeft();
            yield return point.ShiftRight();
            yield return point.ShiftTop();
            yield return point.ShiftBottom();
        }

        private IEnumerable<BoardPoint> EnumeratePoints(BoardElement element) => Enumerable
            .Range(0, BoardString.Length)
            .Where(i => element == (BoardElement)BoardString[i])
            .Select(GetPointByShift);

        private IEnumerable<BoardPoint> EnumeratePoints(HashSet<BoardElement> elements) => Enumerable
            .Range(0, BoardString.Length)
            .Where(i => elements.Contains((BoardElement)BoardString[i]))
            .Select(GetPointByShift);

        private bool HasElementAt(int x, int y, IEnumerable<BoardElement> elements)
            => elements.Any(elem => HasElementAt(x, y, elem));

        private bool HasElementAt(BoardPoint point, params BoardElement[] elements)
        {
            return elements.Any(elem => HasElementAt(point.X, point.Y, elem));
        }

        private int GetShiftByPoint(int x, int y) => y * Size + x;

        private BoardPoint GetPointByShift(int shift) => new BoardPoint(shift % Size, shift / Size);

        private bool IsOutOfBoard(int x, int y) => x >= Size || y >= Size || x < 0 || y < 0;
    }
}
