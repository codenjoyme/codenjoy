using SnakeBattle.Enums;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle.Interfaces
{
    public interface ISolver
    {
        PlayerCommand Decide(IBoardNavigator boardNavigator);
    }
}