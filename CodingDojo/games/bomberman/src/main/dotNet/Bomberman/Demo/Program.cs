/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
        static string Server = "ecsc00104eef.epam.com:8080";    // to use for server on LAN        
        // static string Server = "127.0.0.1:8080";             // to use for localhost server
        // static string Server = "tetrisj.jvmhost.net:12270";  // to use for codenjoy.com server

        static string UserName = "oleksandr_baglai@epam.com";

        static void Main(string[] args)
        {
            // creating custom bomberman's Ai client
            var bomber = new MyCustomBombermanAI(UserName, Server);
            
            // starting thread with playing bomberman
            (new Thread(bomber.Play)).Start();
            
            // waiting for "anykey"
            Console.ReadKey();

            // on "anykey" - asking bomberman's Ai client to stop. 
            bomber.InitiateExit();
        }
    }
}
