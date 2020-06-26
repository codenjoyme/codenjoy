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
#include "stdafx.h"
#include "InputMessage.h"
#include "cpprest/json.h"


InputMessage::InputMessage(const std::string& message) : source{ message }
{
	if (message.size() < 7)
		throw std::runtime_error("Bad input message");

	web::json::value jsonMsg = web::json::value::parse(utility::conversions::to_string_t(message.c_str() + 6));

	myId = jsonMsg[L"myColor"].as_integer();

	web::json::value& myBaseLocationJson = jsonMsg[L"myBase"];
	myBaseLocation.x = myBaseLocationJson[L"x"].as_integer();
	myBaseLocation.y = myBaseLocationJson[L"y"].as_integer();

	availableForces = jsonMsg[L"available"].as_number().to_uint32();

	currentRound = jsonMsg[L"round"].as_integer();
	maxRounds = jsonMsg[L"rounds"].as_integer();

	parseForcesCount(jsonMsg[L"forces"].as_string());
	web::json::value& layers = jsonMsg[L"layers"];
	parseForcesId(layers[1].as_string());
	parseField(layers[0].as_string());
}

void InputMessage::parseForcesCount(const std::wstring& str)
{
	const size_t charsPerItem = 3;
	const std::wstring none{ L"-=#" };

	if (str.size() != getTotalBoardSize()*charsPerItem)
		throw std::runtime_error("Bad input message");

	std::wstring current;
	forcesCount.reserve(getTotalBoardSize());
	for (size_t i = 0; i < str.size(); i += charsPerItem)
	{
		current.assign(str.c_str() + i, str.c_str() + i + charsPerItem);
		if (none == current)
			forcesCount.push_back(0);
		else
			forcesCount.push_back(stoi(current, nullptr, 36));
	}
}

void InputMessage::parseForcesId(const std::wstring& str)
{
	if (str.size() != getTotalBoardSize())
		throw std::runtime_error("Bad input message");

	std::vector<bool> activeEnemyMask(getMaxPlayerCount(), false);
	forcesId.reserve(getTotalBoardSize());
	for (size_t i = 0; i < str.size(); ++i)
	{
		switch (str[i])
		{
		case L'\x2665':
			forcesId.push_back(0);
			activeEnemyMask[0] = true;
			break;
		case L'\x2666':
			forcesId.push_back(1);
			activeEnemyMask[1] = true;
			break;
		case L'\x2663':
			forcesId.push_back(2);
			activeEnemyMask[2] = true;
			break;
		case L'\x2660':
			forcesId.push_back(3);
			activeEnemyMask[3] = true;
			break;
		case L'-':
		default:
			forcesId.push_back(NOBODY);
			break;
		};
	}

	for (size_t i = 0; i < activeEnemyMask.size(); ++i)
	{
		int id = static_cast<int>(i);
		if ( (id != myId) && activeEnemyMask[i] )
			enemyIds.push_back(id);
	}
}

void InputMessage::parseField(const std::wstring& str)
{
	if (str.size() != getTotalBoardSize())
		throw std::runtime_error("Bad input message");

	fieldElements.reserve(getTotalBoardSize());
	for (size_t i = 0; i < str.size(); ++i)
	{
		switch (str[i])
		{
		case L'\x2500':
		case L'\x2502':
		case L'\x250c':
		case L'\x2510':
		case L'\x2514':
		case L'\x2518':
		case L'\x2550':
		case L'\x2551':
		case L'\x2554':
		case L'\x2557':
		case L'\x255a':
		case L'\x255d':
		case L' ':
			fieldElements.push_back(FieldElements::BORDER);
			break;
		case L'B':
		case L'O':
			fieldElements.push_back(FieldElements::OBSTACLE);
			break;
		case L'.':
			fieldElements.push_back(FieldElements::FLOOR);
			break;
		case L'$':
			fieldElements.push_back(FieldElements::GOLD);
			break;
		case L'1':
		case L'2':
		case L'3':
		case L'4':
			bases[str[i] - L'1'] = i;
			fieldElements.push_back(FieldElements::FLOOR);
			break;
		default:
			fieldElements.push_back(FieldElements::OBSTACLE);
			break;
		};
	}
}


std::vector<Point> InputMessage::GetMyForceLocations(void) const
{
	std::vector<Point> myForceLocation;
	myForceLocation.reserve(getTotalBoardSize() / 2);

	for (size_t i = 0; i < forcesId.size(); ++i)
	{
		if (forcesId[i] == myId)
			myForceLocation.push_back(IndexToPoint(i));
	}

	std::sort(myForceLocation.begin(), myForceLocation.end());

	return myForceLocation;
}
