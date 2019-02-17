/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
namespace SnakeClient
{
    public enum Element : short
    {

        BAD_APPLE = (short)'☻',
        GOOD_APPLE = (short)'☺',

        BREAK = (short)'☼',

        HEAD_DOWN = (short)'▼',
        HEAD_LEFT = (short)'◄',
        HEAD_RIGHT = (short)'►',
        HEAD_UP = (short)'▲',

        TAIL_END_DOWN = (short)'╙',
        TAIL_END_LEFT = (short)'╘',
        TAIL_END_UP = (short)'╓',
        TAIL_END_RIGHT = (short)'╕',
        TAIL_HORIZONTAL = (short)'═',
        TAIL_VERTICAL = (short)'║',
        TAIL_LEFT_DOWN = (short)'╗',
        TAIL_LEFT_UP = (short)'╝',
        TAIL_RIGHT_DOWN = (short)'╔',
        TAIL_RIGHT_UP = (short)'╚',

        NONE = (short)' '
    }
    
}
