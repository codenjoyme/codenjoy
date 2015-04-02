using System;
using Bomberman.Api;

namespace Demo
{
    /// <summary>
    /// This is BombermanAI client demo.
    /// </summary>
    internal class MyCustomBombermanAI : BombermanBase
    {
        public MyCustomBombermanAI(string name)
            : base(name)
        {
        }

        /// <summary>
        /// Calls each move to make decision what to do (next move)
        /// </summary>
        protected override string DoMove(GameBoard gameBoard)
        {
            //Just print current state (gameBoard) to console
            Console.SetCursorPosition(0, 0);
            gameBoard.PrintBoard();

			return BombermanActionToString(BombermanAction.GoDown);
        }

        /// <summary>
        /// Starts bomberman's client shutdown.
        /// </summary>
        public void InitiateExit()
        {
            ShouldExit = true;
        }
    }
}