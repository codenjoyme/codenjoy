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
            return ElementGroups.MyFallenBike.Any();
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

        public List<Point> GetOtherHeroes()
        {
            var elementsList = new Element[] {
                Element.OTHER_BIKE,
                Element.OTHER_BIKE_AT_ACCELERATOR,
                Element.OTHER_BIKE_AT_INHIBITOR,
                Element.OTHER_BIKE_AT_KILLED_BIKE,
                Element.OTHER_BIKE_AT_LINE_CHANGER_DOWN,
                Element.OTHER_BIKE_AT_LINE_CHANGER_UP,
                Element.OTHER_BIKE_AT_SPRINGBOARD_LEFT,
                Element.OTHER_BIKE_AT_SPRINGBOARD_LEFT_DOWN,
                Element.OTHER_BIKE_AT_SPRINGBOARD_RIGHT,
                Element.OTHER_BIKE_AT_SPRINGBOARD_RIGHT_DOWN,
                Element.OTHER_BIKE_FALLEN,
                Element.OTHER_BIKE_FALLEN_AT_ACCELERATOR,
                Element.OTHER_BIKE_FALLEN_AT_FENCE,
                Element.OTHER_BIKE_FALLEN_AT_INHIBITOR,
                Element.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_DOWN,
                Element.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_UP,
                Element.OTHER_BIKE_FALLEN_AT_OBSTACLE,
                Element.OTHER_BIKE_IN_FLIGHT_FROM_SPRINGBOARD
            };
            return Get(elementsList);
        }

        public List<Point> GetAccelerators()
        {
            var elementsList = new Element[] {
                Element.ACCELERATOR
            };
            return Get(elementsList);
        }

        public List<Point> GetBikesToKill()
        {
            var elementsList = new Element[] {
                Element.OTHER_BIKE,
                Element.OTHER_BIKE_AT_ACCELERATOR,
                Element.OTHER_BIKE_AT_INHIBITOR,
                Element.OTHER_BIKE_AT_KILLED_BIKE,
                Element.OTHER_BIKE_AT_LINE_CHANGER_DOWN,
                Element.OTHER_BIKE_AT_LINE_CHANGER_UP,
                Element.OTHER_BIKE_AT_SPRINGBOARD_LEFT,
                Element.OTHER_BIKE_AT_SPRINGBOARD_LEFT_DOWN,
                Element.OTHER_BIKE_AT_SPRINGBOARD_RIGHT,
                Element.OTHER_BIKE_AT_SPRINGBOARD_RIGHT_DOWN
            };
            return Get(elementsList);
        }

        public List<Point> GetFences()
        {
            var elementsList = new Element[] {
                Element.FENCE
            };
            return Get(elementsList);
        }

        public List<Point> GetInhibitors()
        {
            var elementsList = new Element[] {
                Element.INHIBITOR
            };
            return Get(elementsList);
        }


        public List<Point> GetLineUpChangers()
        {
            var elementsList = new Element[] {
                Element.LINE_CHANGER_UP
            };
            return Get(elementsList);
        }

        public List<Point> GetLineDownChangers()
        {
            var elementsList = new Element[] {
                Element.LINE_CHANGER_DOWN
            };
            return Get(elementsList);
        }
        public List<Point> GetObstacles()
        {
            var elementsList = new Element[] {
                Element.OBSTACLE
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardDarkElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_LEFT
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardLightElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_RIGHT
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardLeftDownElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_LEFT_DOWN
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardRightDownElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_RIGHT_DOWN
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardLeftUpElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_LEFT_UP
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardRightUpElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_RIGHT_UP
            };
            return Get(elementsList);
        }

        public List<Point> GetSpringboardTopElements()
        {
            var elementsList = new Element[] {
                Element.SPRINGBOARD_TOP
            };
            return Get(elementsList);
        }



        public string GetAllElementsOnBoard()
        {
            Point me = GetMe();
            var otherHeroes = string.Join(',', GetOtherHeroes());
            var accelerators = string.Join(',', GetAccelerators());
            var fences = string.Join(',', GetFences());
            var inhibitors = string.Join(',', GetInhibitors());
            var lineUpChangers = string.Join(',', GetLineUpChangers());
            var lineDownChangers = string.Join(',', GetLineDownChangers());
            var obstacles = string.Join(',', GetObstacles());
            var springboardDarkElements = string.Join(',', GetSpringboardDarkElements());
            var springboardLightElements = string.Join(',', GetSpringboardLightElements());
            var springboardLeftDownElements = string.Join(',', GetSpringboardLeftDownElements());
            var springboardRightDownElements = string.Join(',', GetSpringboardRightDownElements());
            var springboardLeftUpElements = string.Join(',', GetSpringboardLeftUpElements());
            var springboardRightUpElements = string.Join(',', GetSpringboardRightUpElements());
            var springboardTopElements = string.Join(',', GetSpringboardTopElements());

            var result = $"Me at: {me} \n Enemy bikes at: {otherHeroes}\n" +
            $"Accelerators at: {accelerators}\n" +
            $"Fences at: {fences}\n" +
            $"Inhibitors at: {inhibitors}\n" +
            $"Line Up Changers at: {lineUpChangers}\n" +
            $"Line Down Changers at: {lineDownChangers}\n" +
            $"Obstacles at: {obstacles}\n" +
            $"Springboard Dark Elements at: {springboardDarkElements}\n" +
            $"Springboard Light Elements at: {springboardLightElements}\n" +
            $"Springboard Left Down Elements at: {springboardLeftDownElements}\n" +
            $"Springboard Right Down Elements at: {springboardRightDownElements}\n" +
            $"Springboard Left Up Elements at: {springboardLeftUpElements}\n" +
            $"Springboard Right Up Elements at: {springboardRightUpElements}\n" +
            $"Springboard Top Elements at: {springboardTopElements}\n";

            return result;
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
