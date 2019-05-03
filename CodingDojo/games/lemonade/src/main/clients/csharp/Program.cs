/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

namespace LemonadeClient
{
	class Program
	{
		// Server name and port number -- ask orgs
		private static string ServerNameAndPort = "epruryaw0576.moscow.epam.com:43022";
		// Register on the server, write down your registration name
		private static string UserName = "borc1231231236a406kvh";
		// Look up for the code in the browser url after the registration
		private static string UserCode = "1555550509319024989";
		static void Main(string[] args)
		{
			string url = $"ws://{ServerNameAndPort}/codenjoy-contest/ws?user={UserName}&code={UserCode}";
			ClientWrapper client = new ClientWrapper(url, new MyLemonadeBot());

			Console.Read();
		}
	}
}
