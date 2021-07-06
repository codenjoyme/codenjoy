using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using SpaceRace.Api;
using SpaceRace.Player;

namespace SpaceRace.UnitTests
{
    [TestClass]
    public class DebugMySolver
    {
        private List<string> BoardStrings { get; } = new List<string>
        {
            @"☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼                 $          ☼
☼                            ☼
☼            ♣   0           ☼
☼             7              ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0                   ☼
☼                            ☼
☼                            ☼
☼             ☻             0☼
☼                            ☼
☼               ☺  ☻    $    ☼",

            @"☼    $                       ☼
☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼                 $          ☼
☼                            ☼
☼            ♣7  0           ☼
☼                            ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0                   ☼
☼                            ☼
☼            ☻               ☼
☼                           0☼
☼              ☺   ☻         ☼",

            @"☼                   ♣        ☼
☼    $                       ☼
☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼                 $          ☼
☼             7              ☼
☼            ♣   0           ☼
☼                            ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0                   ☼
☼           ☻                ☼
☼                            ☼
☼               ☺  ☻        0☼",
            @"☼                            ☼
☼                   ♣        ☼
☼    $                       ☼
☼               0            ☼
☼                            ☼
☼ ♣                          ☼
☼    0                       ☼
☼             7   $          ☼
☼                            ☼
☼            ♣   0           ☼
☼                            ☼
☼                            ☼
☼              0     $       ☼
☼                 ♣          ☼
☼                            ☼
☼                       0    ☼
☼                            ☼
☼        $     ♣             ☼
☼               0            ☼
☼                            ☼
☼                            ☼
☼          ♣        0        ☼
☼                    $       ☼
☼                            ☼
☼   0                        ☼
☼        ♣                   ☼
☼                            ☼
☼   $    0 ☻                 ☼
☼                            ☼
☼              ☺   ☻         ☼"
        };


        [TestMethod]
        public void DebugMySolverTest()
        {
            var logger = new Logger();
            var solver = new Solver(logger);
            foreach (var boardString in BoardStrings)
            {
                var board = new Board(boardString);
                var result = solver.Get(board);
                logger.LogResult(result);
            }   
        }
    }
}