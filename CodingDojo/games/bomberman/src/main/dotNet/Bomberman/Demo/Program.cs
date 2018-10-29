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

namespace Demo
{
    class Program
    {
        // tetrisj.jvmhost.net:12270  // to use for codenjoy.com server
        // 127.0.0.1:8080               // to use for localhost server
        static string ServerUrl = "http://127.0.0.1:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890";
        
		// you can get this code after registration on the server with your email
		// http://server-ip:8080/codenjoy-contest/board/player/your@email.com?code=12345678901234567890    

        static void Main(string[] args)
        {
            Console.SetWindowSize(Console.LargestWindowWidth - 3, Console.LargestWindowHeight - 3);

            // creating custom bomberman's Ai client


            var bomber = new DashasSolver(ServerUrl);

            // starting thread with playing bomberman
            Thread thread = new Thread(bomber.Play);
            thread.Start();
            thread.Join();
        }
    }
}
