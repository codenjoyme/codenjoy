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

namespace SpaceRace
{
    class Program
    {
        const string ServerAddress = "http://localhost:8080/codenjoy-contest/board/player/t5x9j3cku9wfmxnu67jl?code=2863467953483706002&game=spacerace";

        static void Main(string[] args)
        {
            // creating and starting a bot instance
            using var bot = new MySpaceRaceBot(ServerAddress);

            // waiting for any key
            Console.ReadKey();

            // on any key - asking AI client to stop.
            bot.InitiateExit();
        }
    }
}
