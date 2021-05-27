using System;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using SnakeBattle.Builders;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            var config = new ConfigurationBuilder()
                .AddJsonFile("appsettings.json")
                .Build();

            var serverUrl = config.GetSection("ServerUrl").Value;

            var serviceProvider = ServiceProviderBuilder.Build();

            var snakeBattleClient = serviceProvider.GetRequiredService<ISnakeBattleClient>();

            snakeBattleClient.ConnectAsync(serverUrl);

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