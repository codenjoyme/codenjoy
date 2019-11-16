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
namespace BikeClient.Elements
{
    public class ElementGroups
    {
        public static readonly Element[] AllBikeStates = new[]
        {
            Element.BIKE,
            Element.BIKE_AT_ACCELERATOR,
            Element.BIKE_AT_INHIBITOR,
            Element.BIKE_AT_LINE_CHANGER_UP,
            Element.BIKE_AT_LINE_CHANGER_DOWN,
            Element.BIKE_AT_KILLED_BIKE,
            Element.BIKE_AT_SPRINGBOARD_LEFT,
            Element.BIKE_AT_SPRINGBOARD_LEFT_DOWN,
            Element.BIKE_AT_SPRINGBOARD_RIGHT,
            Element.BIKE_AT_SPRINGBOARD_RIGHT_DOWN,
            Element.BIKE_IN_FLIGHT_FROM_SPRINGBOARD,
            Element.BIKE_FALLEN,
            Element.BIKE_FALLEN_AT_ACCELERATOR,
            Element.BIKE_FALLEN_AT_INHIBITOR,
            Element.BIKE_FALLEN_AT_LINE_CHANGER_UP,
            Element.BIKE_FALLEN_AT_LINE_CHANGER_DOWN,
            Element.BIKE_FALLEN_AT_FENCE,
            Element.BIKE_FALLEN_AT_OBSTACLE,
            Element.OTHER_BIKE,
            Element.OTHER_BIKE_AT_ACCELERATOR,
            Element.OTHER_BIKE_AT_INHIBITOR,
            Element.OTHER_BIKE_AT_LINE_CHANGER_UP,
            Element.OTHER_BIKE_AT_LINE_CHANGER_DOWN,
            Element.OTHER_BIKE_AT_KILLED_BIKE,
            Element.OTHER_BIKE_AT_SPRINGBOARD_LEFT,
            Element.OTHER_BIKE_AT_SPRINGBOARD_LEFT_DOWN,
            Element.OTHER_BIKE_AT_SPRINGBOARD_RIGHT,
            Element.OTHER_BIKE_AT_SPRINGBOARD_RIGHT_DOWN,
            Element.OTHER_BIKE_IN_FLIGHT_FROM_SPRINGBOARD,
            Element.OTHER_BIKE_FALLEN,
            Element.OTHER_BIKE_FALLEN_AT_ACCELERATOR,
            Element.OTHER_BIKE_FALLEN_AT_INHIBITOR,
            Element.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_UP,
            Element.OTHER_BIKE_FALLEN_AT_LINE_CHANGER_DOWN,
            Element.OTHER_BIKE_FALLEN_AT_FENCE,
            Element.OTHER_BIKE_FALLEN_AT_OBSTACLE
        };
        public static readonly Element[] MyBike = new[]
        {
            Element.BIKE,
            Element.BIKE_AT_ACCELERATOR,
            Element.BIKE_AT_INHIBITOR,
            Element.BIKE_AT_LINE_CHANGER_UP,
            Element.BIKE_AT_LINE_CHANGER_DOWN,
            Element.BIKE_AT_KILLED_BIKE,
            Element.BIKE_AT_SPRINGBOARD_LEFT,
            Element.BIKE_AT_SPRINGBOARD_LEFT_DOWN,
            Element.BIKE_AT_SPRINGBOARD_RIGHT,
            Element.BIKE_AT_SPRINGBOARD_RIGHT_DOWN,
            Element.BIKE_IN_FLIGHT_FROM_SPRINGBOARD,
        };
        public static readonly Element[] MyFallenBike = new[]
        {
            Element.BIKE_FALLEN,
            Element.BIKE_FALLEN_AT_ACCELERATOR,
            Element.BIKE_FALLEN_AT_INHIBITOR,
            Element.BIKE_FALLEN_AT_LINE_CHANGER_UP,
            Element.BIKE_FALLEN_AT_LINE_CHANGER_DOWN,
            Element.BIKE_FALLEN_AT_FENCE,
            Element.BIKE_FALLEN_AT_OBSTACLE
        };
    }
}
