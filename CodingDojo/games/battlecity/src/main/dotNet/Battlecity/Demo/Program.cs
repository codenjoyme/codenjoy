using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Battlecity.API;

namespace Demo
{
    class Program
    {
        static string serverURL = "http://localhost:8080/codenjoy-contest/board/player/0?code=000000000000&gameName=battlecity";

        static void Main(string[] args)
        {
            Console.SetWindowSize(Console.LargestWindowWidth - 3, Console.LargestWindowHeight - 3);

            // Creating custom AI client
            var bot = new YourSolver(serverURL);

            // Starting thread with playing game
            Task.Run(bot.Play);
            Console.ReadKey();

            // On any key - asking AI client to stop.
            bot.InitiateExit();
        }
    }
}
