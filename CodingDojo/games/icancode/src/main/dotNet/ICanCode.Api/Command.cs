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

namespace ICanCode.Api
{
    public class Command
    {
        private readonly string command;

        public Command(string command)
        {
            this.command = command;
        }

        /**
        * Says to Hero do nothing
        */
        public static Command DoNothing()
        {
            return new Command(string.Empty);
        }

        /**
         * Reset current level
         */
        public static Command Die()
        {
            return new Command("ACT(0)");
        }

        /**
         * Says to Hero jump to direction
         */
        public static Command Jump(Direction direction)
        {
            return new Command("ACT(1)" + "," + direction.ToString());
        }

        /**
         * Says to Hero pull box on this direction
         */
        public static Command Pull(Direction direction)
        {
            return new Command("ACT(2)" + "," + direction.ToString());
        }

        /**
         * Says to Hero fire on this direction
         */
        public static Command Fire(Direction direction)
        {
            return new Command("ACT(3)" + "," + direction.ToString());
        }

        /**
         * Says to Hero jump in place
         */
        public static Command Jump()
        {
            return new Command("ACT(1)");
        }

        /**
         * Says to Hero go to direction
         */
        public static Command Go(Direction direction)
        {
            return new Command(direction.ToString());
        }

        /**
         * Says to Hero goes to start point
         */
        public static Command Reset()
        {
            return new Command("ACT(0)");
        }

        public override string ToString()
        {
            return command.ToString();
        }
    }
}
