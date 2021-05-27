using System.Threading.Tasks;

namespace SnakeBattle.Interfaces.Services
{
    public interface ISnakeBattleClient
    {
        Task ConnectAsync(string serverUrl);
    }
}