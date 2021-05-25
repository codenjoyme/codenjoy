/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2020 Codenjoy
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
using System.Threading.Tasks;

namespace Battlecity.API
{
    public class Board
    {
        string BoardString { get; }
        LengthToXY lengthXY;

        public Board(string boardString)
        {
            BoardString = boardString.Replace("\n", "");
            lengthXY = new LengthToXY(Size);
        }

        public int Size => (int)Math.Sqrt(BoardString.Length);

        #region Base matrix

        public List<Point> Get(Element element)
        {
            List<Point> result = new List<Point>();

            for (int i = 0; i < Size * Size; i++)
            {
                Point point = lengthXY.GetXY(i);

                if (IsAt(point, element))
                    result.Add(point);
            }

            return result;
        }

        public Element GetAt(Point point) => point.IsOutOf(Size)
            ? Element.WALL
            : BoardString[lengthXY.GetLength(point.X, point.Y)].ToString().GetElement();

        public bool IsAt(Point point, Element element) => point.IsOutOf(Size)
            ? false
            : GetAt(point).GetDescription() == element.GetDescription();

        public bool IsAnyOfAt(Point point, params Element[] elements) => 
            elements.Any(element => IsAt(point, element));

        public bool IsNear(Point point, Element element) => point.IsOutOf(Size)
            ? false
            : IsAt(point.ShiftLeft(), element) || IsAt(point.ShiftRight(), element) 
                || IsAt(point.ShiftTop(), element) || IsAt(point.ShiftBottom(), element);

        public int CountNear(Point point, Element element)
        {
            if (point.IsOutOf(Size))
                return 0;

            int count = 0;

            if (IsAt(point.ShiftLeft(), element)) 
                count++;

            if (IsAt(point.ShiftRight(), element)) 
                count++;

            if (IsAt(point.ShiftTop(), element)) 
                count++;

            if (IsAt(point.ShiftBottom(), element)) 
                count++;

            return count;
        }

        #endregion

        #region Players

        public Point GetMe() => Get(Element.TANK_UP)
            .Concat(Get(Element.TANK_DOWN)).Concat(Get(Element.TANK_LEFT)).Concat(Get(Element.TANK_RIGHT))
            .SingleOrDefault();

        public List<Point> GetEnemies() => Get(Element.AI_TANK_UP)
            .Concat(Get(Element.AI_TANK_DOWN)).Concat(Get(Element.AI_TANK_LEFT)).Concat(Get(Element.AI_TANK_RIGHT))
            .Concat(Get(Element.OTHER_TANK_UP)).Concat(Get(Element.OTHER_TANK_DOWN)).Concat(Get(Element.OTHER_TANK_LEFT))
            .Concat(Get(Element.OTHER_TANK_RIGHT)).Concat(Get(Element.AI_TANK_PRIZE))
            .ToList();

        #endregion

        #region Bullets

        public List<Point> GetBullets() => Get(Element.BULLET);

        public bool IsBulletAt(Point point) => GetAt(point) == Element.BULLET;

        #endregion

        #region Barriers

        public List<Point> GetBarriers() => Get(Element.BATTLE_WALL)
            .Concat(Get(Element.WALL)).Concat(Get(Element.WALL_DESTROYED_DOWN)).Concat(Get(Element.WALL_DESTROYED_UP))
            .Concat(Get(Element.WALL_DESTROYED_LEFT)).Concat(Get(Element.WALL_DESTROYED_RIGHT))
            .Concat(Get(Element.WALL_DESTROYED_DOWN_TWICE)).Concat(Get(Element.WALL_DESTROYED_UP_TWICE))
            .Concat(Get(Element.WALL_DESTROYED_LEFT_TWICE)).Concat(Get(Element.WALL_DESTROYED_RIGHT_TWICE))
            .Concat(Get(Element.WALL_DESTROYED_LEFT_RIGHT)).Concat(Get(Element.WALL_DESTROYED_UP_DOWN))
            .Concat(Get(Element.WALL_DESTROYED_UP_LEFT)).Concat(Get(Element.WALL_DESTROYED_RIGHT_UP))
            .Concat(Get(Element.WALL_DESTROYED_DOWN_LEFT)).Concat(Get(Element.WALL_DESTROYED_DOWN_RIGHT))
            .ToList();

        public bool IsBarrierAt(Point point) => 
            point.IsOutOf(Size) ? true : GetBarriers().Contains(point);

        #endregion

        public bool IsGameOver() => Get(Element.TANK_UP)
            .Concat(Get(Element.TANK_DOWN)).Concat(Get(Element.TANK_LEFT)).Concat(Get(Element.TANK_RIGHT))
            .Count() == 0;

        public string BoardAsString()
        {
            string result = "";

            for (int i = 0; i < Size; i++)
            {
                result += BoardString.Substring(i * Size, Size);
                result += '\n';
            }

            return result;
        }

        public new string ToString()
        {
            string ListToString(List<Point> list) => string.Join(",", list.ToArray());

            return $"{BoardAsString()} \n" +
                $"My tank at: {GetMe()} \n" + 
                $"Enemies at: {ListToString(GetEnemies())} \n" +
                $"Bullets at: {ListToString(GetBullets())} \n";
        }
    }
}
