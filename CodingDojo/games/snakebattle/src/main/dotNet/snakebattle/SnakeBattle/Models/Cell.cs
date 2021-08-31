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
﻿using System;
using SnakeBattle.Enums;

namespace SnakeBattle.Models
{
    public class Cell
    {
        public CellType Type { get; init; }
        public int CoordinateX { get; init; }
        public int CoordinateY { get; init; }

        private bool Equals(Cell other)
        {
            return Type == other.Type && CoordinateX == other.CoordinateX && CoordinateY == other.CoordinateY;
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj))
            {
                return false;
            }

            if (ReferenceEquals(this, obj))
            {
                return true;
            }

            if (obj.GetType() != GetType())
            {
                return false;
            }

            return Equals((Cell) obj);
        }

        public override int GetHashCode()
        {
            return HashCode.Combine((int) Type, CoordinateX, CoordinateY);
        }

        public static bool operator ==(Cell a, Cell b)
        {
            return
                a?.Type == b?.Type
                && a?.CoordinateX == b?.CoordinateX
                && a?.CoordinateY == b?.CoordinateY;
        }

        public static bool operator !=(Cell a, Cell b)
        {
            return !(a == b);
        }

        public override string ToString()
        {
            return $"x: {CoordinateX}, y: {CoordinateY}, {Type.ToString()}";
        }
    }
}
