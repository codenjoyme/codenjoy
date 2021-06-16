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
using System.Collections.Generic;
using SpaceRace.Api;
using SpaceRace.Api.Interfaces;

namespace SpaceRace
{
    /// <summary>
    /// This is SpaceRaceAI client demo.
    /// </summary>
    internal class Solver : ISolver
    {
        private ILogger Logger { get; }

        public Solver(ILogger logger)
        {
            Logger = logger;
        }
        
        /// <summary>
        /// Called each game tick to make decision what to do (next move)
        /// </summary>
        public IDirection Get(Board board)
        {
            //TODO: Implement your logic here
            Random random = new Random(Environment.TickCount);
            var movements = new List<IDirection>()
            {
                Direction.LEFT, Direction.RIGHT, Direction.DOWN, Direction.UP
            };
            
            var action = movements[random.Next(0,4)];
            if (random.Next(0, 1) == 1) action = action.WithAct();
            return action;
        }

    }
}
