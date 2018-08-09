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
using System;

namespace Bomberman.Api
{
    public class LengthToXY
    {
        public int Size;

        public LengthToXY(int size)
        {
            Size = size;
        }

        private int InversionY(int y)
        {
            return Size - 1 - y;
        }

        private int InversionX(int x)
        {
            return x;
        }

        public int GetLength(int x, int y)
        {
            int xx = InversionX(x);
            int yy = InversionY(y);
            return yy * Size + xx;
        }

        public Point GetXY(int length)
        {
            int x = InversionX(length % Size);
            int y = InversionY(length / Size);
            return new Point(x, y);
        }
    }
}
