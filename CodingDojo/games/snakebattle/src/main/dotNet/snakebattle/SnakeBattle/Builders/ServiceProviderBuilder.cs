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