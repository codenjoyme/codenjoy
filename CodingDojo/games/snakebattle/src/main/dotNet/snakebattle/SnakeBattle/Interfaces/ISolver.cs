using System.Collections.Generic;
using SnakeBattle.Enums;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle.Interfaces
{
    public interface ISolver
    {
        IEnumerable<PlayerCommand> Decide(IBoardNavigator boardNavigator);
    }
}