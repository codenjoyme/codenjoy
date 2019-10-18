using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ICanCode.Api
{
    public abstract class AbstractLayeredBoard
    {
        protected int size;
        protected char[,,] field;
        protected JObject source;
        protected List<string> layersString = new List<string>();

        protected AbstractLayeredBoard(string boardString)
        {
            if (boardString.Contains("layer"))
            {
                source = JObject.Parse(boardString);
                string[] layersObjects = source["layers"].Values<string>().ToArray();
                Create(layersObjects);
            }
        }

        private void Create(string[] layers)
        {
            layersString.Clear();
            layersString.AddRange(layers);

            string board = layers[0].Replace("\n", "");
            size = (int)Math.Sqrt(board.Length);
            field = new char[layers.Length, size, size];

            for (int i = 0; i < layers.Length; ++i)
            {
                board = layers[i].Replace("\n", "");

                char[] temp = board.ToCharArray();
                for (int y = 0; y < size; y++)
                {
                    int dy = y * size;
                    for (int x = 0; x < size; x++)
                    {
                        field[i, InversionX(x), InversionY(y)] = temp[dy + x];
                    }
                }
            }
        }

        protected int InversionX(int x)
        {
            return x;
        }

        protected int InversionY(int y)
        {
            return y;
        }

        /**
        * Says if at given position (X, Y) at given layer has given elements.
        *
        * @param numLayer Layer number (from 0).
        * @param x        X coordinate.
        * @param y        Y coordinate.
        * @param elements List of elements that we try to detect on this point.
        * @return true is any of this elements was found.
        */
        protected bool IsAt(int numLayer, int x, int y, params Element[] elements)
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

        /**
        * Says if at given position (X, Y) at given layer has given element.
        *
        * @param numLayer Layer number (from 0).
        * @param x        X coordinate.
        * @param y        Y coordinate.
        * @param element  Elements that we try to detect on this point.
        * @return true is element was found.
        */

        protected bool IsAt(int numLayer, int x, int y, Element element)
        {
            Point pt = new Point(x, y);
            if (pt.IsOutOf(size))
            {
                return false;
            }
            return GetAt(numLayer, x, y) == element;
        }

        /**
        * @param numLayer Layer number (from 0).
        * @param x        X coordinate.
        * @param y        Y coordinate.
        * @return Returns element at position specified.
        */
        protected Element GetAt(int numLayer, int x, int y)
        {
            return (Element)field[numLayer, x, y];
        }

        protected List<Point> Get(int numLayer, params Element[] elements)
        {
            List<Point> result = new List<Point>();
            for (int x = 0; x < size; x++)
            {
                for (int y = 0; y < size; y++)
                {
                    foreach (Element element in elements)
                    {
                        if (field[numLayer, x, y] == (char)element)
                        {
                            result.Add(new Point(x, y));
                        }
                    }
                }
            }
            return result;
        }

        public int Size
        {
            get { return size; }
        }

        protected string boardAsString(int numLayer)
        {
            StringBuilder result = new StringBuilder();
            for (int y = 0; y < size; y++)
            {
                for (int x = 0; x < size; x++)
                {
                    result.Append(field[numLayer, InversionX(x), InversionY(y)]);
                }
                result.Append("\n");
            }
            return result.ToString();
        }

        protected int countLayers()
        {
            return field.GetLength(0);
        }

        public override string ToString()
        {
            StringBuilder result = new StringBuilder("Board:");
            for (int i = 0; i < countLayers(); i++)
            {
                result.Append("\n").Append(boardAsString(i));
            }
            return result.ToString();
        }

        /**
        * Says if near (at left, right, down, up,
        * left-down, left-up, right-down, right-up)
        * given position (X, Y) at given layer exists given element.
        *
        * @param numLayer Layer number (from 0).
        * @param x        X coordinate.
        * @param y        Y coordinate.
        * @param element  Element that we try to detect on near point.
        * @return true is element was found.
        */
        protected bool IsNear(int numLayer, int x, int y, Element element)
        {
            Point pt = new Point(x, y);
            if (pt.IsOutOf(size))
            {
                return false;
            }
            return CountNear(numLayer, x, y, element) > 0;
        }


        /**
         * @param numLayer Layer number (from 0).
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @param element  Element that we try to detect on near point.
         * @return Returns count of elements with type specified near
         * (at left, right, down, up,
         * left-down, left-up, right-down, right-up) {x,y} point.
         */
        protected int CountNear(int numLayer, int x, int y, Element element)
        {
            Point pt = new Point(x, y);
            if (pt.IsOutOf(size))
            {
                return 0;
            }
            List<Element> near = GetNear(numLayer, x, y);
            int count = 0;
            foreach (Element e in near)
            {
                if (e == element) count++;
            }
            return count;
        }

        /**
         * @param numLayer Layer number (from 0).
         * @param x        X coordinate.
         * @param y        Y coordinate.
         * @return All elements around
         * (at left, right, down, up,
         * left-down, left-up, right-down, right-up) position.
         */
        protected List<Element> GetNear(int numLayer, int x, int y)
        {
            List<Element> result = new List<Element>();

            int radius = 1;
            for (int dx = -radius; dx <= radius; dx++)
            {
                for (int dy = -radius; dy <= radius; dy++)
                {
                    Point pt = new Point(x + dx, y + dy);
                    if (pt.IsOutOf(size))
                    {
                        continue;
                    }
                    if (dx == 0 && dy == 0)
                    {
                        continue;
                    }
                    if (WithoutCorners && (dx != 0 && dy != 0))
                    {
                        continue;
                    }
                    result.Add(GetAt(numLayer, x + dx, y + dy));
                }
            }

            return result;
        }

        protected bool WithoutCorners => false;
    }
}
