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
// ExpansionClient.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include "WebSocketClient.h"
#include "StringMessagesHandler.h"

int main()
{
	WebSocketClient<StringMessageHandler> client(L"ws://127.0.0.1:8080/codenjoy-contest/ws", L"bot1@mail.com");
//	WebSocketClient<StringMessageHandler> client(L"ws://codenjoy.com:80/codenjoy-contest/ws", L"bot1@mail.com");

	client.Run();

	return 0;
}
