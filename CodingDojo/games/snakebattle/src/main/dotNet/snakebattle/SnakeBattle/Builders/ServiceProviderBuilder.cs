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
                    IgnoreRoundExceptions = bool.Parse(configuration["IgnoreRoundException"])
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