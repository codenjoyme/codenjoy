/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
using System.Text;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using NLog;

namespace ExpansionBot
{
    public class Board
    {
        private const string Base36CharList = "0123456789abcdefghijklmnopqrstuvwxyz";

        public static Point[] Moves = new[]
        {
            new Point(1, 0),  // Right
            new Point(0, -1), // Down
            new Point(0, 1),  // Up
            new Point(-1, 0), // Left
            new Point(-1, -1),// LeftDown
            new Point(-1, 1), // LeftUp
            new Point(1, 1),  // RightUp
            new Point(1, -1), // RightDown
        };

        public int MyColor { get; private set; }

        public Point MyBase { get; private set; }

        public string Forces { get; private set; }

        public string[] Layers { get; private set; }

        public void Parse(string input)
        {
            if (input.StartsWith("board={"))
                input = input.Substring(6);

            JObject jobj = JObject.Parse(input);
            //log.Debug(jobj.ToString());

            MyColor = int.Parse(jobj["myColor"].ToString());

            MyBase = new Point(
                int.Parse(jobj["myBase"]["x"].ToString()),
                int.Parse(jobj["myBase"]["y"].ToString()));

            Forces = jobj["forces"].ToString();

            List<string> layers = new List<string>();
            foreach (var jtlayer in jobj["layers"].Children())
            {
                layers.Add(jtlayer.ToString());
            }
            Layers = layers.ToArray();
        }

        public bool IsBlockAt(int x, int y)
        {
            int mapsize = (int)Math.Sqrt(Forces.Length / 3);

            char ch = Layers[0][x + (mapsize - y - 1) * mapsize];
            const string nonblock = ".1234$";

            return !nonblock.Contains(ch);
        }

        public List<Forces> GetMyForces()
        {
            int mapsize = (int) Math.Sqrt(Forces.Length / 3);

            List<Forces> my = new List<Forces>();
            string layer1 = Layers[1];

            for (int y = 0; y < mapsize; y++)
            {
                for (int x = 0; x < mapsize; x++)
                {
                    char ch = layer1[y * mapsize + x];
                    if (ch == '-')
                        continue;
                    int chcolor = GetColorByForcesChar(ch);
                    if (chcolor >= 0 && chcolor == MyColor)
                    {
                        string sfcount = Forces.Substring(y*mapsize*3 + x*3, 3);
                        int fcount = Base36Decode(sfcount);
                        Forces item = new Forces(fcount, new Point(x, mapsize - y - 1), MyColor);
                        my.Add(item);
                    }
                }
            }

            return my;
        }

        public List<Forces> GetEnemyForces()
        {
            int mapsize = (int)Math.Sqrt(Forces.Length / 3);

            List<Forces> my = new List<Forces>();
            string layer1 = Layers[1];

            for (int y = 0; y < mapsize; y++)
            {
                for (int x = 0; x < mapsize; x++)
                {
                    char ch = layer1[y * mapsize + x];
                    if (ch == '-')
                        continue;
                    int chcolor = GetColorByForcesChar(ch);
                    if (chcolor >= 0 && chcolor != MyColor)
                    {
                        string sfcount = Forces.Substring(y * mapsize * 3 + x * 3, 3);
                        int fcount = Base36Decode(sfcount);
                        Forces item = new Forces(fcount, new Point(x, mapsize - y - 1), chcolor);
                        my.Add(item);
                    }
                }
            }

            return my;
        }

        public List<Direction> FindShortestPath(Point ptsrc, Point ptdest)
        {
            int mapsize = (int)Math.Sqrt(Forces.Length / 3);

            int[,] map = new int[mapsize, mapsize];
            for (int y = 0; y < mapsize; y++)
            {
                for (int x = 0; x < mapsize; x++)
                {
                    bool isblock = IsBlockAt(x, y);
                    map[x, y] = isblock ? -1 : int.MaxValue;
                }
            }

            int valdest = map[ptdest.x, ptdest.y];
            if (valdest == -1)
                return null;  // Destination is a block/hole etc.

            // Flood phase
            map[ptsrc.x, ptsrc.y] = 0;
            for (int step = 0; step < mapsize*2; step++)
            {
                for (int y = 1; y < mapsize - 1; y++)
                {
                    for (int x = 1; x < mapsize - 1; x++)
                    {
                        int val = map[x, y];
                        if (val == -1 || val == int.MaxValue)
                            continue;

                        for (int dir = 0; dir < 8; dir++)
                        {
                            Point move = Moves[dir];
                            Point next = new Point(x + move.x, y + move.y);
                            int val2 = map[next.x, next.y];
                            if (val2 != int.MaxValue)
                                continue;
                            map[next.x, next.y] = val + 1;
                        }
                    }
                }

                valdest = map[ptdest.x, ptdest.y];
                if (valdest != int.MaxValue)
                    break;
            }

            valdest = map[ptdest.x, ptdest.y];
            if (valdest == int.MaxValue)
                return null;  // Destination is not reachable

            // Backtrace
            List<Direction> result = new List<Direction>();
            Point ptcur = ptdest;
            int valcur = valdest;
            while (true)
            {
                for (int dir = 0; dir < 8; dir++)
                {
                    Point move = Moves[dir];
                    Point next = new Point(ptcur.x - move.x, ptcur.y - move.y);
                    if (next.x < 0 || next.x >= mapsize || next.y < 0 || next.y >= mapsize)
                        continue;
                    int val = map[next.x, next.y];
                    if (val == -1)
                        continue;
                    if (val < valcur)
                    {
                        ptcur = next;
                        valcur = val;
                        result.Insert(0, (Direction)(dir + 1));
                        break;
                    }
                }
                if (valcur == 0)
                    break;
            }

            return result;
        }

        private static int GetColorByForcesChar(char ch)
        {
            int chcolor = -1;
            switch (ch)
            {
                case '\u2665': chcolor = 0; break;
                case '\u2666': chcolor = 1; break;
                case '\u2663': chcolor = 2; break;
                case '\u2660': chcolor = 3; break;
            }
            return chcolor;
        }

        private static int Base36Decode(string input)
        {
            if (input == "-=#")
                return 0;

            var reversed = input.ToLower().Reverse();
            int result = 0;
            int pos = 0;
            foreach (char c in reversed)
            {
                result += Base36CharList.IndexOf(c) * (int)Math.Pow(36, pos);
                pos++;
            }
            return result;
        }
    }
}
