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
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Battlecity.API
{
    public enum Element
    {
        [Description(" ")]
        NONE,
        [Description("☼")]
        BATTLE_WALL,
        [Description("Ѡ")]
        BANG,

        [Description("#")]
        ICE,
        [Description("%")]
        TREE,
        [Description("~")]
        RIVER,

        [Description("╬")]
        WALL,

        [Description("╩")]
        WALL_DESTROYED_DOWN,
        [Description("╦")]
        WALL_DESTROYED_UP,
        [Description("╠")]
        WALL_DESTROYED_LEFT,
        [Description("╣")]
        WALL_DESTROYED_RIGHT,

        [Description("╨")]
        WALL_DESTROYED_DOWN_TWICE,
        [Description("╥")]
        WALL_DESTROYED_UP_TWICE,
        [Description("╞")]
        WALL_DESTROYED_LEFT_TWICE,
        [Description("╡")]
        WALL_DESTROYED_RIGHT_TWICE,

        [Description("│")]
        WALL_DESTROYED_LEFT_RIGHT,
        [Description("─")]
        WALL_DESTROYED_UP_DOWN,

        [Description("┌")]
        WALL_DESTROYED_UP_LEFT,
        [Description("┐")]
        WALL_DESTROYED_RIGHT_UP,
        [Description("└")]
        WALL_DESTROYED_DOWN_LEFT,
        [Description("┘")]
        WALL_DESTROYED_DOWN_RIGHT,

        [Description(" ")]
        WALL_DESTROYED,

        [Description("•")]
        BULLET,

        [Description("▲")]
        TANK_UP,
        [Description("►")]
        TANK_RIGHT,
        [Description("▼")]
        TANK_DOWN,
        [Description("◄")]
        TANK_LEFT,

        [Description("˄")]
        OTHER_TANK_UP,
        [Description("˃")]
        OTHER_TANK_RIGHT,
        [Description("˅")]
        OTHER_TANK_DOWN,
        [Description("˂")]
        OTHER_TANK_LEFT,

        [Description("?")]
        AI_TANK_UP,
        [Description("»")]
        AI_TANK_RIGHT,
        [Description("¿")]
        AI_TANK_DOWN,
        [Description("«")]
        AI_TANK_LEFT,

        [Description("◘")]
        AI_TANK_PRIZE,

        [Description("!")]
        PRIZE,
        [Description("1")]
        PRIZE_IMMORTALITY,
        [Description("2")]
        PRIZE_BREAKING_WALLS,
        [Description("3")]
        PRIZE_WALKING_ON_WATER,
        [Description("4")]
        PRIZE_VISIBILITY,
        [Description("5")]
        PRIZE_NO_SLIDING
    }
}
