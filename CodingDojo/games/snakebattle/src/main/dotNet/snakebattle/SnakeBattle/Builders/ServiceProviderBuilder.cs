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
using System.Diagnostics.CodeAnalysis;
using Microsoft.Extensions.DependencyInjection;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Interfaces.Utilities;
using SnakeBattle.Services;
using SnakeBattle.Utilities;

namespace SnakeBattle.Builders
{
    [ExcludeFromCodeCoverage]
    public static class ServiceProviderBuilder
    {
        private static void AddServices(IServiceCollection serviceCollection)
        {
            serviceCollection.AddTransient<ISolver, Solver>();
            serviceCollection.AddTransient<IDisplayService, DisplayService>();
            serviceCollection.AddTransient<ISnakeBattleClient, SnakeBattleClient>();
            serviceCollection.AddTransient<IBoardStringParser, BoardStringParser>();
        }

        public static IServiceProvider Build()
        {
            var serviceCollection = new ServiceCollection();

            AddServices(serviceCollection);

            var serviceProvider = serviceCollection.BuildServiceProvider();

            return serviceProvider;
        }
    }
}
