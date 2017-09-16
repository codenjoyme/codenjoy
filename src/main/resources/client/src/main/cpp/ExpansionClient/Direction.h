#pragma once

enum class Direction : unsigned 
{
	UNKNOWN,
	RIGHT,
	DOWN,
	UP,
	LEFT,
	RIGHT_UP,
	RIGHT_DOWN,
	LEFT_UP,
	LEFT_DOWN
};

const wchar_t *const DirectionNames[] = {
	L"UNKNOWN",
	L"RIGHT",
	L"DOWN",
	L"UP",
	L"LEFT",
	L"RIGHT_UP",
	L"RIGHT_DOWN",
	L"LEFT_UP",
	L"LEFT_DOWN"
};

inline std::wstring getDirectionName(Direction direction)
{
	if (static_cast<size_t>(direction) < sizeof(DirectionNames)/sizeof(DirectionNames[0]))
		return DirectionNames[static_cast<size_t>(direction)];
	else
		return L"Something went wrong";
}

const Direction allGoodDirections[] = { Direction::RIGHT, Direction::DOWN, Direction::UP, Direction::LEFT,
	Direction::RIGHT_UP, Direction::RIGHT_DOWN, Direction::LEFT_UP, Direction::LEFT_DOWN };
