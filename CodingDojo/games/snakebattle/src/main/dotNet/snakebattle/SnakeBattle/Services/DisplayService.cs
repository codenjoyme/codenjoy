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
﻿using System;
using System.Text;
using SnakeBattle.Interfaces.Services;

namespace SnakeBattle.Services
{
    public class DisplayService : IDisplayService
    {
        public DisplayService()
        {
            Console.OutputEncoding = Encoding.UTF8;
        }

        public void Render(string board, string botCommand)
        {
            Console.Clear();
            Console.SetCursorPosition(0, 0);
            Console.WriteLine(FormatBoardString(board));

            Console.WriteLine($"Decision: {botCommand}.");
            Console.WriteLine("Press 'X' to exit.");
            Console.SetCursorPosition(0, 0);
        }

        private static string FormatBoardString(string board)
        {
            var boardSize = (int) Math.Sqrt(board.Length);

            var result = string.Empty;

            for (var i = 0; i < boardSize; i++)
            {
                result += board.Substring(i * boardSize, boardSize);
                result += '\n';
            }

            return result;
        }
    }
}
