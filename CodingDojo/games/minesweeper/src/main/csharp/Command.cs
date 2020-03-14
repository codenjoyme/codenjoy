/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
﻿namespace MinesweeperClient
{
    public struct Command
    {
        public readonly Direction Direction;
        public readonly bool IsAction;

        private Command(Direction direction, bool isAction)
        {
            Direction = direction;
            IsAction = isAction;
        }

        public override string ToString()
        {
            return IsAction && Direction != Direction.Act ? string.Format("{0}, {1}", Direction.Act.ToString(), Direction.ToString()) : Direction.ToString();
        }

        public static Command MoveTo(Direction direction)
        {
            return new Command(direction, false);
        }

        public static Command SetFlagTo(Direction direction)
        {
            return new Command(direction, true);
        }

        public static Command CreateCommand(Direction direction, bool isAction)
        {
            return new Command(direction, isAction);
        }
    }
}
