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
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Serilog;
using SnakeBattle.Interfaces;
using SnakeBattle.Interfaces.Services;
using SnakeBattle.Interfaces.Utilities;
using SnakeBattle.Models;
using SnakeBattle.Services;
using SnakeBattle.Utilities;

namespace SnakeBattle.Builders
{
    [ExcludeFromCodeCoverage]
    public static class ServiceProviderBuilder
    {
        public static IServiceProvider Build()
        {
            var serviceCollection = new ServiceCollection();

            RegisterConfiguration(serviceCollection);
            RegisterGameConfiguration(serviceCollection);
            RegisterServices(serviceCollection);
            RegisterLogger(serviceCollection);

            var serviceProvider = serviceCollection.BuildServiceProvider();

            return serviceProvider;
        }

        private static void RegisterConfiguration(IServiceCollection serviceCollection)
        {
            var configuration = new ConfigurationBuilder()
                .AddJsonFile("appsettings.json")
                .Build();

            serviceCollection.AddTransient<IConfiguration>(provider => configuration);
        }

        private static void RegisterGameConfiguration(IServiceCollection serviceCollection)
        {
            serviceCollection.AddSingleton(provider =>
            {
                var configuration = provider.GetRequiredService<IConfiguration>();

                var gameConfiguration = new GameConfiguration
                {
                    ServerUrl = configuration["ServerUrl"],
                    IgnoreRoundExceptions = bool.Parse(configuration["IgnoreSolverExceptions"]),
                    KeepConsoleHistory = bool.Parse(configuration["KeepConsoleHistory"])
                };

                return gameConfiguration;
            });
        }

        private static void RegisterLogger(IServiceCollection serviceCollection)
        {
            var loggerConfiguration = new LoggerConfiguration();
            var logger = loggerConfiguration
                .WriteTo.File("SnakeBattle.log")
                .WriteTo.Debug()
                .CreateLogger();

            serviceCollection.AddLogging(builder => builder.AddSerilog(logger));
        }

        private static void RegisterServices(IServiceCollection serviceCollection)
        {
            serviceCollection.AddTransient<ISolver, Solver>();
            serviceCollection.AddTransient<IDisplayService, DisplayService>();
            serviceCollection.AddTransient<ISnakeBattleClient, SnakeBattleClient>();
            serviceCollection.AddTransient<IBoardStringParser, BoardStringParser>();
        }
    }
}