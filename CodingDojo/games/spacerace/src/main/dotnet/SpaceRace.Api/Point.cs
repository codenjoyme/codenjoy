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

namespace SpaceRace.Api
{
    public struct Point
    {
        public readonly int X;
        public readonly int Y;

        public Point(int x, int y)
        {
            X = x;
            Y = y;
        }

        /// <summary>
        /// Checks is current point on board or out of range.
        /// </summary>
        /// <param name="boardSize">Board size to compare</param>
        public bool IsOutOfBoard(int boardSize)
        {
            return X >= boardSize || Y >= boardSize || X < 0 || Y < 0;
        }

        /// <summary>
        /// Returns new Point object shifted left to "delta" points
        /// </summary>
        public Point ShiftLeft(int delta = 1)
        {
            return new Point(X - delta, Y);
        }

        /// <summary>
        /// Returns new Point object shifted right to "delta" points
        /// </summary>
        public Point ShiftRight(int delta = 1)
        {
            return new Point(X + delta, Y);
        }

        /// <summary>
        /// Returns new Point object shifted top "delta" points
        /// </summary>
        public Point ShiftTop(int delta = 1)
        {
            return new Point(X, Y - delta);
        }

        /// <summary>
        /// Returns new Point object shifted bottom "delta" points
        /// </summary>
        public Point ShiftBottom(int delta = 1)
        {
            return new Point(X, Y + delta);
        }

        public static bool operator ==(Point p1, Point p2)
        {
            return p1.X == p2.X && p1.Y == p2.Y;
        }

        public static bool operator !=(Point p1, Point p2)
        {
            return !(p1 == p2);
        }

        public override string ToString()
        {
            return string.Format("[{0},{1}]", X, Y);
        }

        public bool Equals(Point other)
        {
            return X == other.X && Y == other.Y;
        }

        public override bool Equals(object obj)
        {
            return obj is Point other && Equals(other);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return (X * 397) ^ Y;
            }
        }
    }
}
