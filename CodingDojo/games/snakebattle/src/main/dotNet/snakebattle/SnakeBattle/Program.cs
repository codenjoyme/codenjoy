/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using SnakeBattle.Builders;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            var serviceProvider = ServiceProviderBuilder.Build();
            
            try
            {
                var snakeBattleClient = serviceProvider.GetRequiredService<ISnakeBattleClient>();
                snakeBattleClient.Connect();
            }
            catch (Exception exception)
            {
                Console.WriteLine(exception);
                
                var logger = serviceProvider.GetRequiredService<ILogger<Program>>();
                logger.LogCritical(exception, "Critical error during game client work");
                
                throw;
            }

            while (true)
            {
                var consoleKeyInfo = Console.ReadKey(true);
                if (consoleKeyInfo.Key == ConsoleKey.X)
                {
                    break;
                }
            }
        }
    }
}