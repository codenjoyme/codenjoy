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
using System.Collections.Generic;
using System.Linq;
using BikeClient.Elements;

namespace BikeClient.GameArea
{
    public abstract class Board : AbstractPlayGround
    {
        public Point GetMe()
        {
            return Get(ElementGroups.MyBike).FirstOrDefault();
        }

        public bool IsGameOver()
        {
            Point me = GetMe();
            return ElementGroups.MyFallenBike.Any(x => IsAt(me, x));
        }

        public bool CheckNearMe(List<QDirection> directions, Element[] elements)
        {
            Point point = GetMe();
            if (point == null)
            {
                return false;
            }

            foreach (QDirection direction in directions)
            {
                point = direction.Change(point);
            }

            return IsAt(point.X, point.Y, elements);
        }

        public bool CheckNearMe(QDirection direction, Element[] elements)
        {
            Point me = GetMe();
            if (me == null)
            {
                return false;
            }

            Point atDirection = direction.Change(me);
            return IsAt(atDirection.X, atDirection.Y, elements);
        }

        public bool CheckAtMe(Element[] elements)
        {
            Point me = GetMe();
            return me != null && IsAt(me, elements);
        }

        public bool IsOutOfFieldRelativeToMe(QDirection direction)
        {
            Point me = GetMe();
            if (me == null)
            {
                return false;
            }

            Point atDirection = direction.Change(me);
            return IsOutOfField(atDirection.X, atDirection.Y);
        }
    }
}
