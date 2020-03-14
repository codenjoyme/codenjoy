/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
﻿namespace MinesweeperClient
{
    public struct PointExtendView
    {
        public readonly int X;
        public readonly int Y;
        public readonly Element Element;

        public PointExtendView(int x, int y, Element element)
        {
            X = x;
            Y = y;
            Element = element;
        }

        /// <summary>
        /// Checks is current point on board or out of range.
        /// </summary>
        /// <param name="size">Board size to compare</param>
        public bool IsOutOf(int size)
        {
            return X >= size || Y >= size || X < 0 || Y < 0;
        }

        /// <summary>
        /// Returns new BoardPoint object shifted left to "delta" points
        /// </summary>
        public Point ShiftLeft(int delta = 1)
        {
            return new Point(X - delta, Y);
        }

        /// <summary>
        /// Returns new BoardPoint object shifted right to "delta" points
        /// </summary>
        public Point ShiftRight(int delta = 1)
        {
            return new Point(X + delta, Y);
        }

        /// <summary>
        /// Returns new BoardPoint object shifted top "delta" points
        /// </summary>
        public Point ShiftTop(int delta = 1)
        {
            return new Point(X, Y + delta);
        }

        /// <summary>
        /// Returns new BoardPoint object shifted bottom "delta" points
        /// </summary>
        public Point ShiftBottom(int delta = 1)
        {
            return new Point(X, Y - delta);

        }

        public Point Shift(Direction direction)
        {
            switch (direction)
            {
                case Direction.Left:
                    return ShiftLeft();
                case Direction.Right:
                    return ShiftRight();
                case Direction.Up:
                    return ShiftTop();
                case Direction.Down:
                    return ShiftBottom();
                case Direction.Act:
                    return new Point(X, Y);
                default:
                    return new Point(X, Y);
            }
        }

        public static bool operator ==(PointExtendView p1, PointExtendView p2)
        {
            if (ReferenceEquals(p1, p2))
                return true;

            if (ReferenceEquals(p1, null) || ReferenceEquals(p2, null))
                return false;

            return p1.X == p2.X && p1.Y == p2.Y;
        }

        public static bool operator !=(PointExtendView p1, PointExtendView p2)
        {
            return !(p1 == p2);
        }

        public override string ToString()
        {
            return string.Format("[{0},{1},({2})]", X, Y, Element.ToString());
        }

        public override bool Equals(object obj)
        {
            if (obj == null) return false;
            if (!(obj is PointExtendView)) return false;

            PointExtendView that = (PointExtendView)obj;

            return that.X == X && that.Y == Y;
        }

        public override int GetHashCode()
        {
            return (X.GetHashCode() ^ Y.GetHashCode());
        }
    }
}
