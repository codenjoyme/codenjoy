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
namespace A2048.Api
{
    public enum Element : short
    {
        /// <summary>
        /// This is breaks
        /// </summary>
        _x = (short)'x',

        /// <summary>
        /// This is number
        /// </summary>
        _2 = (short)'2',
        _4 = (short)'4',
        _8 = (short)'8',
        _16 = (short)'A',
        _32 = (short)'B',
        _64 = (short)'C',
        _128 = (short)'D',
        _256 = (short)'E',
        _512 = (short)'F',
        _1024 = (short)'G',
        _2048 = (short)'H',
        _4096 = (short)'I',
        _8192 = (short)'J',
        _16384 = (short)'K',
        _32768 = (short)'L',
        _65536 = (short)'M',
        _131072 = (short)'N',
        _262144 = (short)'O',
        _524288 = (short)'P',
        _1048576 = (short)'Q',
        _2097152 = (short)'R',
        _4194304 = (short)'S',

        /// <summary>
        /// Empty space on a map. This is the only place where you can move your numbers
        /// </summary>
        NONE = (short)' '

    }
}
