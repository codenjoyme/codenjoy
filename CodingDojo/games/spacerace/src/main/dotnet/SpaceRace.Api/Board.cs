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

namespace SpaceRace.Api
{
    public class Board
    {
        private Dictionary<Point, Element> _boardAsDictionary = null; 
        
        public Board(string boardString)
        {
            BoardString = boardString.Replace("\n", "");
            Size = (int) Math.Sqrt(BoardString.Length);
        }

        public string BoardString { get; }

        public int Size { get; }

        /// <summary>
        /// Transform game board to dictionary Point -> Element. Useable to LINQ and lists manipulations
        /// </summary>
        /// <returns>board as a dictionary</returns>
        public Dictionary<Point, Element> GetAllExtend()
        {
            return _boardAsDictionary ?? (_boardAsDictionary = Enumerable
                .Range(0, BoardString.Length)
                .ToDictionary(
                    GetPointByShift,
                    i => (Element) BoardString[i]
                ));
        }

        /// <summary>
        /// Returns the list of points for the given element type.
        /// </summary>
        /// <param name="elements"></param>
        /// <returns></returns>
        public List<Point> FindAll(params Element[] elements) =>
            EnumeratePoints(new HashSet<Element>(elements))
            .ToList();

        /// <summary>
        /// Returns the point where your hero is.
        /// </summary>
        public Point GetMyPosition() => EnumeratePoints(Element.Hero).Single();

        /// <summary>
        /// Returns a Element object at coordinates x,y.
        /// </summary>
        /// <param name="point"></param>
        /// <returns></returns>
        public Element GetAt(int x, int y) => (Element)BoardString[GetShiftByPoint(x, y)];

        /// <summary>
        /// Returns True if Element is at x,y coordinates.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <param name="element"></param>
        /// <returns></returns>
        public bool HasElementAt(int x, int y, Element element)
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
        public bool IsBarrierAt(int x, int y) => HasElementAt(x, y, Element.Wall);

        /// <summary>
        /// Returns False if your hero still alive.
        /// </summary>
        /// <returns></returns>
        public bool IsHeroDead() => BoardString.Contains((char)Element.DeadHero);

        /// <summary>
        /// Return the list of points for other heroes.
        /// </summary>
        /// <returns></returns>
        public List<Point> GetEnemyPositions() => EnumeratePoints(Element.OtherHero).ToList();

        /// <summary>
        /// Returns the list of points for other heroes.
        /// </summary>
        /// <returns></returns>
        public List<Point> GetOtherHeroPositions() => EnumeratePoints(Element.OtherHero).ToList();

        /// <summary>
        /// Returns the list of walls element Points.
        /// </summary>
        /// <returns></returns>
        public List<Point> GetWallPositions() => EnumeratePoints(Element.Wall).ToList();

        /// <summary>
        /// Returns the list of barriers Points.
        /// </summary>
        /// <returns></returns>
        public List<Point> GetBarriers() => EnumeratePoints(Element.Wall).ToList();

        /// <summary>
        /// Check if near exists element of chosen type.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <param name="element"></param>
        /// <returns></returns>
        public bool IsNearToElement(int x, int y, Element element)
        {
            if (IsOutOfBoard(x, y))
                return false;

            return EnumerateNeighbors(new Point(x, y))
                .Any(neighbor => HasElementAt(neighbor, element));
        }

        /// <summary>
        /// Returns True if enemy exists in current point.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool HasEnemyAt(int x, int y) => HasElementAt(x, y, Element.OtherHero);

        /// <summary>
        /// Returns True if other hero exists in current point.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool HasOtherHeroAt(int x, int y) => HasElementAt(x, y, Element.OtherHero);

        /// <summary>
        /// Returns True if wall exists in current point.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <returns></returns>
        public bool HasWallAt(int x, int y) => HasElementAt(x, y, Element.Wall);

        /// <summary>
        /// Counts the number of occurrences of element nearby.
        /// </summary>
        /// <param name="x"></param>
        /// <param name="y"></param>
        /// <param name="element"></param>
        /// <returns></returns>
        public int GetCountElementsNearToPoint(int x, int y, Element element)
        {
            if (IsOutOfBoard(x, y))
                return 0;

            return EnumerateNeighbors(new Point(x, y))
                .Count(neighbor => HasElementAt(neighbor, element));
        }

        public override string ToString()
        {
            var sb = new StringBuilder();
            sb.Append("Board:\n");
            for (int i = 0; i < Size; i++)
            {
                sb.Append(BoardString.Substring(i * Size, Size));
                sb.Append("\n");
            }

            return sb.ToString();
        }

        private IEnumerable<Point> EnumerateNeighbors(Point point)
        {
            yield return point.ShiftLeft();
            yield return point.ShiftRight();
            yield return point.ShiftTop();
            yield return point.ShiftBottom();
        }

        private IEnumerable<Point> EnumeratePoints(Element element) => Enumerable
            .Range(0, BoardString.Length)
            .Where(i => element == (Element)BoardString[i])
            .Select(GetPointByShift);

        private IEnumerable<Point> EnumeratePoints(HashSet<Element> elements) => Enumerable
            .Range(0, BoardString.Length)
            .Where(i => elements.Contains((Element)BoardString[i]))
            .Select(GetPointByShift);

        private bool HasElementAt(int x, int y, IEnumerable<Element> elements)
            => elements.Any(elem => HasElementAt(x, y, elem));

        private bool HasElementAt(Point point, params Element[] elements)
        {
            return elements.Any(elem => HasElementAt(point.X, point.Y, elem));
        }

        private int GetShiftByPoint(int x, int y) => y * Size + x;

        private Point GetPointByShift(int shift) => new Point(shift % Size, shift / Size);

        private bool IsOutOfBoard(int x, int y) => x >= Size || y >= Size || x < 0 || y < 0;
    }
}
