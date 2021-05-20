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
    public class LengthToXY
    {
        public int Size;

        public LengthToXY(int size) =>
            Size = size;

        int InversionY(int y) => Size - 1 - y;

        int InversionX(int x) => x;

        public int GetLength(int x, int y) => InversionY(y) * Size + InversionX(x);

        public Point GetXY(int length) => 
            new Point(InversionX(length % Size), InversionY(length / Size));
    }
}
