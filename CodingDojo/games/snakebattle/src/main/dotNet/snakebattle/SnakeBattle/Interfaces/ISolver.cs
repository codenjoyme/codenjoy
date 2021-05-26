using SnakeBattle.Enums;
using SnakeBattle.Interfaces.Models;

namespace SnakeBattle.Interfaces
{
    public interface ISolver
    {
        PlayerCommand Decide(IBoard board);
    }
}