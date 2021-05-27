using SnakeBattle.Services;

namespace SnakeBattle.Interfaces.Utilities
{
    public interface IBoardStringParser
    {
        BoardNavigator Parse(string boardString);
    }
}