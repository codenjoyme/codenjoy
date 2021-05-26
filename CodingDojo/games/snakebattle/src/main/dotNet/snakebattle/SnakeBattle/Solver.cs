using System;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Models;

namespace SnakeBattle
{
    public class Solver : ISolver
    {
        public PlayerCommand Decide(IBoard board)
        {
            // Todo Do your magic!
            var values = Enum.GetValues(typeof(PlayerCommand));
            var random = new Random();
            var randomBotCommand = (PlayerCommand) values.GetValue(random.Next(values.Length));

            return randomBotCommand;
        }
    }
}