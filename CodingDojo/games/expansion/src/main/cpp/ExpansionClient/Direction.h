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
