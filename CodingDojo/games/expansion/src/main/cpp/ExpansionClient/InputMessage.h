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
#include <vector>
#include <map>
#include "Point.h"
#include "Direction.h"

enum class FieldElements
{
	UNKOWN,
	BORDER,
	OBSTACLE,
	FLOOR,
	GOLD
};

class InputMessage
{
public:
	InputMessage(const std::string& message);

	int getMyId(void) const { return myId; }
	std::vector<int> getEnemyIds(void) const { return enemyIds; }

	Point getMyBaseLocation(void) const { return myBaseLocation; }

	unsigned getAvailableForces(void) const { return availableForces; }

	unsigned getCurrentRound(void) const { return currentRound; }
	unsigned getMaxRounds(void) const { return maxRounds; }

	unsigned getXBoardSize(void) const { return 20; }
	unsigned getYBoardSize(void) const { return 20; }
	unsigned getTotalBoardSize(void) const { return getXBoardSize()*getYBoardSize(); }

	unsigned getMaxPlayerCount(void) const { return 4; }


	std::vector<Point> GetMyForceLocations(void) const;
	unsigned getForcesSize(const Point& pt) const { return forcesCount[PointToIndex(pt)]; }
	bool isMyCell(const Point& pt) const { return forcesId[PointToIndex(pt)] == myId; }

	bool isEnemyCell(const Point& pt) const {
		return (forcesId[PointToIndex(pt)] != myId) && (forcesId[PointToIndex(pt)] != NOBODY); }

	bool isWalkableCell(const Point& pt) const {
		FieldElements el = fieldElements[PointToIndex(pt)];
		return (el == FieldElements::FLOOR) || (el == FieldElements::GOLD); }

	bool isGoldCell(const Point& pt) const {
		return fieldElements[PointToIndex(pt)] == FieldElements::GOLD; }

private:
	unsigned PointToIndex(Point pt) const {
		return (getYBoardSize() - 1 - pt.y)*getXBoardSize() + pt.x; }
	Point IndexToPoint(unsigned index) const {
		return Point{ static_cast<int>(index % getXBoardSize()), static_cast<int>(getYBoardSize() -1 - index / getXBoardSize()) }; }

	void parseForcesCount(const std::wstring& str);
	void parseForcesId(const std::wstring& str);
	void parseField(const std::wstring& str);

private:
	const std::string& source;

	int myId;
	std::vector<int> enemyIds;

	Point myBaseLocation;

	unsigned availableForces;

	unsigned currentRound;
	unsigned maxRounds;

	std::vector<unsigned> forcesCount;
	std::vector<int> forcesId;

	std::vector<FieldElements> fieldElements;
	std::map<int, unsigned> bases;

private:
	static const int NOBODY{ 99 };
};
