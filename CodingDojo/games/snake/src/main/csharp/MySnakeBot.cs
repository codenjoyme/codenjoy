/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

namespace SnakeClient
{
    internal class MySnakeBot
    {
        public string HeadlineText { get; private set; }
        public string DisplayText { get; private set; }
        public string CommandText { get; private set; }

        public string Process(string input)
        {
            HeadlineText = "";
            DisplayText = "";

            Board board = new Board();
            board.Parse(input);
            DisplayText = board.GetDisplay();

            Random random = new Random((int)DateTime.Now.Ticks);
            int move = random.Next(0, 3);
            switch (move)
            {
                case 0:
                    CommandText = "UP";
                    break;
                case 1:
                    CommandText = "DOWN";
                    break;
                case 2:
                    CommandText = "LEFT";
                    break;
                case 3:
                    CommandText = "RIGHT";
                    break;
            }

            return CommandText;
        }
    }
}
