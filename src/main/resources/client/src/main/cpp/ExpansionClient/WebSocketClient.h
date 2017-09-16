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
