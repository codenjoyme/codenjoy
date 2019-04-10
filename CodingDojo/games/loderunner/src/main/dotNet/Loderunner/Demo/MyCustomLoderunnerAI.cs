/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
using System;
using Loderunner.Api;

namespace Demo
{
    /// <summary>
    /// This is LoderunnerAI client demo.
    /// </summary>
    internal class MyCustomLoderunnerAI : LoderunnerBase
    {
        public MyCustomLoderunnerAI(string serverUrl)
            : base(serverUrl)
        {
        }

        /// <summary>
        /// Calls each move to make decision what to do (next move)
        /// </summary>
        protected override string DoMove(GameBoard gameBoard)
        {
            //Just print current state (gameBoard) to console
            Console.Clear();
            //Console.SetCursorPosition(0, 0);
            gameBoard.PrintBoard();

            Random random = new Random(Environment.TickCount);

            //TODO: Implement your logic here
            LoderunnerAction action = (LoderunnerAction)random.Next(3);

            Console.WriteLine(action.ToString());
            return LoderunnerActionToString(action);
        }

        /// <summary>
        /// Starts loderunner's client shutdown.
        /// </summary>
        public void InitiateExit()
        {
            ShouldExit = true;
        }
    }
}
