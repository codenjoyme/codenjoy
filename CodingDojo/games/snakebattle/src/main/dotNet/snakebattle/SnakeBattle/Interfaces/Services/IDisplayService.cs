using System;

namespace SnakeBattle.Interfaces.Services
{
    public interface IDisplayService
    {
        void RenderBoard(string board, string playerCommands);
        void ShowError(string message);
        void ShowError(Exception message);
    }
}