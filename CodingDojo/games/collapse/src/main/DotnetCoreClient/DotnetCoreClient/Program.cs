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

namespace CollapseClient
{
	class Program
	{
		static string ServerUrl = "http://localhost/codenjoy-contest/board/player/klsrwt11nd80l7tq16om?code=4155411881026279699";

		// ссылку можно взять в строке браузера после регистрации на сервере и логина
		// http://server-ip:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890

		static void Main(string[] args)
		{
			Console.SetWindowSize(Console.LargestWindowWidth - 3, Console.LargestWindowHeight - 3);

			// создаем экземпляр игрового бота
			var tetrisPlayer = new YourSolver(ServerUrl);

			// запускаем бота в отдельном потоке
			Thread thread = new Thread(tetrisPlayer.Play);
			thread.Start();
			thread.Join();
		}
	}
}
