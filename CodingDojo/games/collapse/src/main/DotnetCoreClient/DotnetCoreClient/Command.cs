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
using System.Collections.Generic;

namespace CollapseClient
{
	public class Command
	{
		public static readonly Command LEFT = new Command("LEFT");
		public static readonly Command UP = new Command("UP");
		public static readonly Command RIGHT = new Command("RIGHT");
		public static readonly Command DOWN = new Command("DOWN");
		public static readonly Command SUICIDE = new Command("ACT(0,0)");

		private readonly List<string> _commandChain = new List<string>();

		private Command(string command)
		{
			_commandChain.Add(command);
		}

		private Command()
		{
		}

		public static Command CreateAct(int x, int y)
		{
			return new Command($"ACT({x},{y})");
		}

		public static Command Combine(Command act, Command direction)
		{
			var chained = new Command();
			chained._commandChain.AddRange(act._commandChain);
			chained._commandChain.AddRange(direction._commandChain);
			return chained;
		}

		public override string ToString()
		{
			return string.Join(",", _commandChain);
		}
	}
}
