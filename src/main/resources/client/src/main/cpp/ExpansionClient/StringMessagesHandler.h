#pragma once

#include <string>


class StringMessageHandler
{
public:
	StringMessageHandler() {}

	std::string Handle(const std::string& message, bool& finish);

private:
	int testCountdown{ 100 };
};
