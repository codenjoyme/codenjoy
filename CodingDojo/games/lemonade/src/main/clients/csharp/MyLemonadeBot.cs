/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
using Newtonsoft.Json;

namespace LemonadeClient
{
	public class MyLemonadeBot
	{
		private Board _BOARD;
		private const string _RESET_COMMAND = "message('go reset')";

		private string GetCommandForServer(int amountOfGlassesToMake, int amountOfSignsToMake, int lemonadeGlassPriceInCents)
		{
			if (amountOfGlassesToMake >= 0 && amountOfGlassesToMake <= 1000
				&& amountOfSignsToMake >= 0 && amountOfSignsToMake <= 50
				&& lemonadeGlassPriceInCents >= 0 && lemonadeGlassPriceInCents <= 100
			)
				return $"message('go {amountOfGlassesToMake}, {amountOfSignsToMake}, {lemonadeGlassPriceInCents}')";

			throw new ArgumentException(
				$"Wrong argument list. amountOfGlassesToMake has to be between 0 and 1000, " +
				$"amountOfSignsToMake has to be between 0 and 50, " +
				$"lemonadeGlassPriceInCents has to be between 0 and 100, " +
				$"but it were {amountOfGlassesToMake}, {amountOfSignsToMake}, {lemonadeGlassPriceInCents}"
			);
		}

		private string GetResetStateCommand()
		{
			return _RESET_COMMAND;
		}

		public string CommandText { get; private set; }

		public void Received(string input)
		{
			if (input.StartsWith("board="))
				input = input.Substring(6);
			_BOARD = JsonConvert.DeserializeObject<Board>(input);
		}

		public string CurrentState
		{
			get { return _BOARD.Messages; }
		}

		public string Process()
		{
			if (!_BOARD.IsBankrupt)
			{
				CommandText = GetCommandForServer(1, 1, 10);
			}
			else
			{
				CommandText = GetResetStateCommand();
			}

			return CommandText;
		}
	}
}
