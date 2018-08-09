/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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
#ifndef BOARD_H
#define BOARD_H

#include "utils.h"

#include "Point.h"
#include "LengthToXY.h"
#include "../Element.h"

class Board {
public:
	Board(String boardString = LL(""));

	Element getAt(int x, int y) const;
	bool isAt(int x, int y, Element el) const;
	bool isAt(int x, int y, std::list<Element> els) const;

	Point getBomberman() const;
	PointList getOtherBombermans() const;
	bool isMyBombermanDead() const;

	int boardSize() const;

	PointList getBarriers() const;
	String toString() const;

	PointList getMeatChoppers() const;

	PointList getWalls() const;
	PointList getDestoyWalls() const;
	PointList getBombs() const;
	PointList getBlasts() const;

	PointList getFutureBlasts() const;

	bool isNear(int x, int y, Element el) const;
	int countNear(int x, int y, Element el) const;

	bool isBarrierAt(int x, int y) const;
private:
	PointList findAll(Element el) const;
	PointList removeDuplicates(PointList lst) const;
	String boardAsString() const;

	String board;
	int size;
	LengthToXY xyl;
};

#endif
