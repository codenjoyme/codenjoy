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
using System.Threading;
using SpaceRace.Api;

namespace SpaceRace
{
    /// <summary>
    /// This is SpaceRaceAI client demo.
    /// </summary>
    internal class MySpaceRaceBot : SpaceRaceBase
    {
	    public MySpaceRaceBot(string serverUrl)
            : base(serverUrl)
	    {
	    }

        /// <summary>
        /// Called each game tick to make decision what to do (next move)
        /// </summary>
        protected override string DoMove(GameBoard gameBoard)
        {
            //Just print current state (gameBoard) to console
            Console.Clear();
            gameBoard.PrintBoard();

            //TODO: Implement your logic here
            Random random = new Random(Environment.TickCount);
            RacerAction action = (RacerAction)random.Next(0,5);

            string actionString = RacerActionToString(action);

            Console.WriteLine(actionString);
            return actionString;
        }

        /// <summary>
        /// Starts SpaceRace's client shutdown.
        /// </summary>
        public void InitiateExit()
        {
            _cts.Cancel();
        }
    }
}
