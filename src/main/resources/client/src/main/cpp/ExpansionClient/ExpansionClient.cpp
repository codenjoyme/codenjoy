// ExpansionClient.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"

#include "WebSocketClient.h"
#include "StringMessagesHandler.h"

int main()
{
	WebSocketClient<StringMessageHandler> client(L"ws://127.0.0.1:8080/codenjoy-contest/ws", L"bot1@mail.com");
//	WebSocketClient<StringMessageHandler> client(L"ws://ecsc00104eef.epam.com:8080/codenjoy-contest/ws", L"bot1@mail.com");

	client.Run();

	return 0;
}
