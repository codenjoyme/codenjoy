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