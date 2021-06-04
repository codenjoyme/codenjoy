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
using System.Text;
using SpaceRace.Api;

namespace SpaceRace
{
    class Program
    {
        static void Main(string[] args)
        {
            // creating and starting a bot instance
            Console.OutputEncoding = Encoding.Unicode;
            
            var logger = new Logger();
            var bot = new Solver(logger);
            using var api = new Api.Api(
                Configuration.ConnectionString, 
                Configuration.ReconnectionIntervalMS, 
                bot,
                logger);

            // waiting for any key
            Console.ReadKey();

            // on any key - asking AI client to stop.
            api.Stop();
        }
    }
}
