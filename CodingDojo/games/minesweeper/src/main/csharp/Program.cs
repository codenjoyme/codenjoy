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
using System;
using System.Threading;

namespace MinesweeperClient
{
	class Program
	{
		static string ServerUrl = "http://localhost:8080/codenjoy-contest/board/player/eosfdc3azokxvkp04knn?code=5662119423876020191";

		// you can get this code after registration on the server with your email
		// http://server-ip:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890

		static void Main(string[] args)
		{
			Console.SetWindowSize(Console.LargestWindowWidth - 3, Console.LargestWindowHeight - 3);

			// creating custom Minesweeper's Ai client
			
			var minesweeper = new YourSolver(ServerUrl);

			// starting thread with playing Minesweeper
			Thread thread = new Thread(minesweeper.Play);
			thread.Start();
			thread.Join();
		}
	}
}
