using System;
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