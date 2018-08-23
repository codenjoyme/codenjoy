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
﻿using System;

namespace LunoletClient
{
    internal class Board
    {
        public int Level { get; set; }

        public double Time { get; set; }

        public double X { get; set; }

        public double Y { get; set; }

        public double HSpeed { get; set; }

        public double VSpeed { get; set; }

        public double FuelMass { get; set; }

        public string State { get; set; }

        public Point2D[] Relief { get; set; }

        public Point2D[] History { get; set; }

        public double Angle { get; set; }

        public Point2D Target { get; set; }
    }
}
