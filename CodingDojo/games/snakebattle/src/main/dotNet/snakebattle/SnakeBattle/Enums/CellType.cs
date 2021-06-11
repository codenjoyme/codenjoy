/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
using System.ComponentModel;

namespace SnakeBattle.Enums
{
    public enum CellType
    {
        [Description(" ")] None,
        [Description("☼")] Wall,
        [Description("#")] StartFloor,
        [Description("○")] Apple,
        [Description("●")] Stone,
        [Description("©")] FlyingPill,
        [Description("®")] FuryPill,
        [Description("$")] Gold,
        [Description("▼")] HeadDown,
        [Description("◄")] HeadLeft,
        [Description("►")] HeadRight,
        [Description("▲")] HeadUp,
        [Description("☻")] HeadDead,
        [Description("♥")] HeadEvil,
        [Description("♠")] HeadFly,
        [Description("&")] HeadSleep,
        [Description("╙")] TailEndDown,
        [Description("╘")] TailEndLeft,
        [Description("╓")] TailEndUp,
        [Description("╕")] TailEndRight,
        [Description("~")] TailInactive,
        [Description("═")] BodyHorizontal,
        [Description("║")] BodyVertical,
        [Description("╗")] BodyLeftDown,
        [Description("╝")] BodyLeftUp,
        [Description("╔")] BodyRightDown,
        [Description("╚")] BodyRightUp,
        [Description("˅")] EnemyHeadDown,
        [Description("<")] EnemyHeadLeft,
        [Description(">")] EnemyHeadRight,
        [Description("˄")] EnemyHeadUp,
        [Description("☺")] EnemyHeadDead,
        [Description("♣")] EnemyHeadEvil,
        [Description("♦")] EnemyHeadFly,
        [Description("ø")] EnemyHeadSleep,
        [Description("¤")] EnemyTailEndDown,
        [Description("×")] EnemyTailEndLeft,
        [Description("æ")] EnemyTailEndUp,
        [Description("ö")] EnemyTailEndRight,
        [Description("*")] EnemyTailInactive,
        [Description("─")] EnemyBodyHorizontal,
        [Description("|")] EnemyBodyVertical,
        [Description("┐")] EnemyBodyLeftDown,
        [Description("┘")] EnemyBodyLeftUp,
        [Description("┌")] EnemyBodyRightDown,
        [Description("└")] EnemyBodyRightUp,
        [Description("?")] Unknown
    }
}