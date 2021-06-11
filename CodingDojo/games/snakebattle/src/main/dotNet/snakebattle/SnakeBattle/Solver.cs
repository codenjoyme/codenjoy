using System;
using System.Collections.Generic;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle
{
    public class Solver : ISolver
    {
        public IEnumerable<PlayerCommand> Decide(IBoardNavigator boardNavigator)
        {
            // Todo Make your magic here!

            return new[] {Random()};
        }

        private static PlayerCommand Random()
        {
            var values = Enum.GetValues(typeof(PlayerCommand));
            var random = new Random();
            var randomBotCommand = (PlayerCommand) values.GetValue(random.Next(values.Length));

            return randomBotCommand;
        }
    }
}