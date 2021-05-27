using System;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle
{
    public class Solver : ISolver
    {
        public PlayerCommand Decide(IBoardNavigator boardNavigator)
        {
            // Todo Make your magic here!

            var values = Enum.GetValues(typeof(PlayerCommand));
            var random = new Random();
            var randomBotCommand = (PlayerCommand) values.GetValue(random.Next(values.Length));

            return randomBotCommand;
        }
    }
}