/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
using Newtonsoft.Json.Linq;

namespace BikeClient
{
    public class Point : ICloneable, IComparable
    {
        public int X { get; set; }
        public int Y { get; set; }

        public Point() : this(-1, -1)
        {
        }

        public Point(int x, int y)
        {
            this.X = x;
            this.Y = y;
        }

        public Point(Point point)
        {
            X = point.X;
            Y = point.Y;
        }

        public Point(JObject json)
        {
            X = (int) json.GetValue("x");
            Y = (int) json.GetValue("y");
        }

        public bool ItsMe(Point pt)
        {
            return ItsMe(pt.X, pt.Y);
        }

        public bool ItsMe(int x, int y)
        {
            return this.X == x && this.Y == y;
        }

        public bool IsOutOf(int size)
        {
            return IsOutOf(0, 0, size);
        }

        public bool IsOutOf(int dw, int dh, int size)
        {
            return X < dw || Y < dh || Y > size - 1 - dh || X > size - 1 - dw;
        }

        public double Distance(Point other)
        {
            return Math.Sqrt((X - other.X) * (X - other.X) + (Y - other.Y) * (Y - other.Y));
        }

        public override int GetHashCode()
        {
            return X * 1000 + Y;
        }

        public override string ToString()
        {
            return string.Format("[{0},{1}]", X, Y);
        }

        public int CompareTo(object o)
        {
            if (o == null)
            {
                return -1;
            }

            return this.GetHashCode().CompareTo(o.GetHashCode());
        }

        public object Clone()
        {
            return new Point(this);
        }

        public override bool Equals(Object o)
        {
            if (o == this)
            {
                return true;
            }

            if (o == null)
            {
                return false;
            }

            if (!(o is Point))
            {
                return false;
            }

            Point p = (Point) o;

            return (p.X == X && p.Y == Y);
        }

        public void Move(int x, int y)
        {
            this.X = x;
            this.Y = y;
        }

        public void Move(Point pt)
        {
            this.X = pt.X;
            this.Y = pt.Y;
        }

        public void Change(Point delta)
        {
            X += delta.X;
            Y += delta.Y;
        }

        public void change(QDirection direction)
        {
            this.Move(direction.Change(this));
        }

        public static Point pt(int x, int y)
        {
            return new Point(x, y);
        }
    }
}
