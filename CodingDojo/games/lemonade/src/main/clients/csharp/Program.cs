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
using System.Linq;
using System.Web;

namespace LemonadeClient
{
    class Program
    {
        static void Main(string[] args)
        {
            // you can get this URL after registration on the server with your email
            string serverUrl = "http://localhost:8080/codenjoy-contest/board/player/a1b2c3d4e5f6g7i8j9k0?code=1234567890123456789";

            var uri = new Uri(serverUrl);
            var server = string.Format("{0}:{1}", uri.Host, uri.Port);
            var userName = uri.Segments.Last();
            var code = HttpUtility.ParseQueryString(uri.Query).Get("code");
            var wsurl = string.Format("ws://{0}/codenjoy-contest/ws?user={1}&code={2}",
                server, Uri.EscapeDataString(userName), code);

            ClientWrapper client = new ClientWrapper(wsurl, new MyLemonadeBot());

            Console.Read();
        }
    }
}
