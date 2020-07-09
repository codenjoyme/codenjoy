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
#pragma once

#include <string>

#include <iostream>
#include <mutex>
#include <condition_variable>

#include "cpprest/ws_client.h"


template<typename THandler>
class WebSocketClient
{
public:
	WebSocketClient(const std::wstring& url, const std::wstring& user)
		: url(url), user(user)
	{
		
	}

	void Run()
	{
		auto connectionString = url + L"?user=" + user;

		std::wcout << L"Connecting to the server " << connectionString << std::endl;

		web::websockets::client::websocket_callback_client client;
		client.connect(connectionString).then([]() {
			std::cout << "Successfully connected to the server" << std::endl;
		});

		std::mutex exitMutex;
		std::condition_variable exitCondition;
		bool exit = false;

		THandler handler;

		std::unique_lock<std::mutex> exitLock(exitMutex);

		client.set_message_handler([&handler, &client, &exitMutex, &exitCondition, &exit](web::websockets::client::websocket_incoming_message msg)
		{
			// handle message from server...
			msg.extract_string()
				.then([&handler, &client, &exitMutex, &exitCondition, &exit](const std::string& messageString) {
					std::cout << "Message received: \n" << messageString << "\n";

					auto finish = false;
					auto response = handler.Handle(messageString, finish);

					web::websockets::client::websocket_outgoing_message msg;
					msg.set_utf8_message(response);
					client.send(msg).then([response]() {
						std::cout << "Response successfully sent: \n" << response << "\n\n";
					});

					if (finish)
					{
						std::cout << "Closing client..." << std::endl;

						{
							std::unique_lock<std::mutex> exitLock(exitMutex);
							exit = true;
						}

						exitCondition.notify_one();
					}
				});
		});

		exitCondition.wait(exitLock, [&exit]{ return exit; });
	}

private:
	std::wstring url;
	std::wstring user;
};
