/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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
﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using NLog;
using Newtonsoft.Json;

namespace ExpansionBot
{
    public class MyBot
    {
        private readonly NLog.ILogger log = LogManager.GetCurrentClassLogger();

        public string Process(string input)
        {
            log.Debug(input);

            Board board = new Board();
            board.Parse(input);
            //log.Debug(JsonConvert.SerializeObject(board.Layers[1]));

            var enemyforces = board.GetEnemyForces();
            log.Debug(JsonConvert.SerializeObject(enemyforces));
            var myforces = board.GetMyForces();
            log.Debug(JsonConvert.SerializeObject(myforces));

            var mysorted = myforces.OrderByDescending(o => o.Count);
            var enemysorted = enemyforces.OrderByDescending(o => o.Count);

            var myfirst = mysorted.First();
            var enemyfirst = enemyforces.First();

            Command command = new Command();

            //int seeds = 10;
            //Random rand = new Random((int)DateTime.Now.Ticks);
            //foreach (Forces myforce in myforces)
            //{
            //    if (myforce.Count == 1)
            //    {
            //        if (seeds > 0)
            //        {
            //            command.AddIncrease(2, myforce.Region);
            //            seeds -= 2;
            //        }
            //        continue;
            //    }

            //    int dir = rand.Next(7);
            //    Point move = Board.Moves[dir];
            //    bool isblock = board.IsBlockAt(myforce.Region.x + move.x, myforce.Region.y + move.y);
            //    if (!isblock)
            //        command.AddMovement(20, myforce.Region, (Direction)(dir + 1));
            //}

            //command.AddIncrease(100, board.MyBase);
            command.AddIncrease(10, myfirst.Region);
            //command.AddMovement(1, board.MyBase, Direction.RightUp);
            //command.AddMovement(100, new Point(1, 2), Direction.Up);

            var thepath = board.FindShortestPath(myfirst.Region, enemyfirst.Region);
            if (thepath != null)
            {
                Direction dir = thepath.First();
                command.AddMovement(myfirst.Count + 10, myfirst.Region, dir);
            }

            string scommand = "message(\'" + command.GenerateCommand() + "\')";
            //log.Info(scommand);
            return scommand;
        }
    }
}
