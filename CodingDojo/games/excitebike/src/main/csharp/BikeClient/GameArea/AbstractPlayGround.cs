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
using System.Collections.Generic;
using System.Linq;
using System.Text;
using BikeClient.Elements;
using Newtonsoft.Json.Linq;

namespace BikeClient
{
    public abstract partial class AbstractPlayGround
    {
        protected char[,] field;
        protected int Size;
        protected JObject source;
        protected List<string> layersString = new List<string>();
        protected const int MAX_Y_SIZE = 12;
        public AbstractPlayGround ForString(string boardString)
        {
            if (boardString.Contains("layer"))
            {
                source = new JObject(boardString);
                var layers = source.GetValue("layers").Select(x => new String(x.Value<string>())).ToArray();
                return ForString(layers);
            }
            else
            {
                return ForString(new string[] { boardString });
            }
        }
        protected bool IsAt(int numLayer, int x, int y, Element element)
        {
            if (Point.pt(x, y).IsOutOf(Size))
            {
                return false;
            }
            return GetAt(numLayer, x, y).Equals(element);
        }
        public AbstractPlayGround ForString(string[] layers)
        {
            layersString.Clear();
            layersString.AddRange(layers);

            String board = layers[0].Replace("\n", "");
            Size = (int)Math.Sqrt(board.Length);
            field = new char[Size, Size];
            char[] temp = board.ToCharArray();
            for (int y = 0; y < Size; y++)
            {
                int dy = y * Size;
                for (int x = 0; x < Size; x++)
                {
                    field[inversionX(x), inversionY(y)] = temp[dy + x];
                }
            }
            return this;
        }
        protected int inversionX(int x)
        {
            return x;
        }

        protected int inversionY(int y)
        {
            return Size - y - 1;
        }

        protected bool IsAt(int numLayer, int x, int y, Element[] elements)
        {
            foreach (var element in elements)
            {
                if (IsAt(numLayer, x, y, element))
                {
                    return true;
                }
            }

            return false;
        }

        protected Element GetAt(int numLayer, int x, int y)
        {
            
            return ValueOf(field[x, y]);
        }

        protected bool IsNear(int numLayer, int x, int y, Element element)
        {
            if (Point.pt(x, y).IsOutOf(Size))
            {
                return false;
            }

            return CountNear(numLayer, x, y, element) > 0;
        }

        protected int CountNear(int numLayer, int x, int y, Element element)
        {
            if (Point.pt(x, y).IsOutOf(Size))
            {
                return 0;
            }

            return GetNear(numLayer, x, y).Where(it => it.Equals(element)).Count();
        }

        protected List<Element> GetNear(int numLayer, int x, int y)
        {
            List<Element> result = new List<Element>();

            int radius = 1;
            for (int dx = -radius; dx <= radius; dx++)
            {
                for (int dy = -radius; dy <= radius; dy++)
                {
                    if (Point.pt(x + dx, y + dy).IsOutOf(Size))
                    {
                        continue;
                    }

                    if (dx == 0 && dy == 0)
                    {
                        continue;
                    }

                    if (WithoutCorners() && (dx != 0 && dy != 0))
                    {
                        continue;
                    }

                    result.Add(GetAt(numLayer, x + dx, y + dy));
                }
            }

            return result;
        }

        protected bool WithoutCorners()
        {
            return false;
        }

        protected void Set(int x, int y, char ch)
        {
            field[x, y] = ch;
        }

        public bool IsOutOfField(int x, int y)
        {
            return Point.pt(x, y).IsOutOf(Size);
        }
        public bool IsAt(int x, int y, Element element)
        {
            if (IsAt(0, x, y, element))
                {
                    return true;
                }
            return false;
        }

        public bool IsAt(Point point, Element element)
        {
            return IsAt(point.X, point.Y, element);
        }

        public Element GetAt(int x, int y)
        {
            if (Point.pt(x, y).IsOutOf(Size))
            {
                return Element.FENCE;
            }
            return GetAt(0, x, y);
        }

        public Element GetAt(Point point)
        {
            return GetAt(point.X, point.Y);
        }

        public List<Element> GetAllAt(int x, int y)
        {
            List<Element> result = new List<Element>();
            result.Add(GetAt(x, y));
            return result;
        }

        public List<Element> GetAllAt(Point point)
        {
            return GetAllAt(point.X, point.Y);
        }

        public static Element ValueOf(char ch)
        {
            return (Element) ch;
        }

        public string boardAsString()
        {
            StringBuilder result = new StringBuilder();
            for (int y = MAX_Y_SIZE; y >=0 ; y--)
            {
                result.Append(y);
                result.Append(" ");
                for (int x = 0; x < Size; x++)
                    result.Append(field[x, y]);
                result.Append("\n");
            }
            return result.ToString();
        }

        

        /**
         * Says if at given position (X, Y) at given layer has given elements.
         * @param x X coordinate.
         * @param y Y coordinate.
         * @param elements List of elements that we try to detect on this point.
         * @return true is any of this elements was found.
         */
        public bool IsAt(int x, int y, Element[] elements)
        {
            if (IsAt(0, x, y, elements))
            {
                return true;
            }
            return false;
        }

        public bool IsAt(Point point, Element[] elements)
        {
            return IsAt(point.X, point.Y, elements);
        }

        /**
         * Says if near (at left, at right, at up, at down) given position (X, Y) at given layer exists given element.
         * @param x X coordinate.
         * @param y Y coordinate.
         * @param element Element that we try to detect on near point.
         * @return true is element was found.
         */
        public bool IsNear(int x, int y, Element element)
        {
            if (IsNear(0, x, y, element))
                {
                    return true;
                }
            return false;
        }

        public bool IsNear(Point point, Element element)
        {
            return IsNear(point.X, point.Y, element);
        }

        /**
         * @param x X coordinate.
         * @param y Y coordinate.
         * @param element Element that we try to detect on near point.
         * @return Returns count of elements with type specified near
         * (at left, right, down, up, left-down, left-up,
         *     right-down, right-up) {x,y} point.
         */
        public int CountNear(int x, int y, Element element)
        {
            int count = 0;
            count += CountNear(0, x, y, element);
                return count;
        }

        public int CountNear(Point point, Element element)
        {
            return CountNear(point.X, point.Y, element);
        }

        /**
         * @param x X coordinate.
         * @param y Y coordinate.
         * @return All elements around (at left, right, down, up,
         *     left-down, left-up, right-down, right-up) position.
         */
        public List<Element> GetNear(int x, int y)
        {
            List<Element> result = new List<Element>();
            result.AddRange(GetNear(x, y));
            return result;
        }

        public List<Element> GetNear(Point point)
        {
            return GetNear(point.X, point.Y);
        }

        /**
         * @param numLayer Layer number (from 0).
         * @param elements List of elements that we try to find.
         * @return All positions of element specified.
         */
        protected List<Point> Get(Element[] elements)
        {
            List<Point> result = new List<Point>();
            for (int x = 0; x < Size; x++)
            {
                for (int y = 0; y < Size; y++)
                {
                    foreach (var element in elements)
                    {
                        if (ValueOf(field[x,y]).Equals(element))
                        {
                            result.Add(Point.pt(x, y));
                        }
                    }
                }
            }

            return result;
        }
    }
}
