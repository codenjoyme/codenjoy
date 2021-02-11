/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
using System.Collections.Generic;
using System.Text;

namespace ExpansionBot
{
    public class Command
    {
        public struct Increase
        {
            public int Count;
            public Point Region;
        }

        public struct Movement
        {
            public int Count;
            public Direction Direction;
            public Point Region;
        }

        private readonly List<Increase> increases = new List<Increase>();
        private readonly List<Movement> movements = new List<Movement>();

        public void AddIncrease(int count, Point region)
        {
            Increase inc;
            inc.Count = count;
            inc.Region = region;
            increases.Add(inc);
        }

        public void AddMovement(int count, Point region, Direction direction)
        {
            Movement mov;
            mov.Count = count;
            mov.Region = region;
            mov.Direction = direction;
            movements.Add(mov);
        }

        public string GenerateCommand()
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("{");
            sb.Append("\"movements\":[");
            for (int index = 0; index < movements.Count; index++)
            {
                if (index > 0) sb.Append(",");
                Movement movement = movements[index];
                sb.Append("{");
                sb.Append("\"count\":");
                sb.Append(movement.Count);
                sb.Append(",\"region\":{\"x\":");
                sb.Append(movement.Region.x);
                sb.Append(",\"y\":");
                sb.Append(movement.Region.y);
                sb.Append("},\"direction\":");
                sb.AppendFormat("\"{0}\"", DirectionToString(movement.Direction));
                sb.Append("}");
            }
            sb.Append("],");
            sb.Append("\"increase\":[");
            for (int index = 0; index < increases.Count; index++)
            {
                if (index > 0) sb.Append(",");
                Increase increase = increases[index];
                sb.Append("{");
                sb.Append("\"count\":");
                sb.Append(increase.Count);
                sb.Append(",\"region\":{\"x\":");
                sb.Append(increase.Region.x);
                sb.Append(",\"y\":");
                sb.Append(increase.Region.y);
                sb.Append("}}");
            }
            sb.Append("]");
            sb.Append("}");
            return sb.ToString();
        }

        private string DirectionToString(Direction direction)
        {
            switch (direction)
            {
                case Direction.Left: return "LEFT";
                case Direction.Down: return "DOWN";
                case Direction.Up: return "UP";
                case Direction.Right: return "RIGHT";
                case Direction.RightDown: return "RIGHT_DOWN";
                case Direction.RightUp: return "RIGHT_UP";
                case Direction.LeftDown: return "LEFT_DOWN";
                case Direction.LeftUp: return "LEFT_UP";
                default:
                    return string.Empty;
            }
        }
    }
}
