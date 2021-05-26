using SnakeBattle.Models;

namespace SnakeBattle.Interfaces.Utilities
{
    public interface IBoardStringParser
    {
        Board Parse(string boardString);
    }
}