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
#include "Point.h"


Point::Point(int _x, int _y) : x(_x), y(_y), isSetAsNull(false) {
}

bool Point::operator==(const Point& pt) const {
	return equals(pt);
}

bool Point::equals(const Point& pt) const {
	return x == pt.x && y == pt.y;
}

bool Point::isNull() const {
	return isSetAsNull;
}

void Point::setNull(bool state) {
	isSetAsNull = state;
}

String Point::toString() const {
	StringStream ss;
	ss << "[" << x << "," << y << "]";
	return ss.str();
}

bool Point::isBad(int boardSize) const {
	return x >= boardSize || y >= boardSize || x < 0 || y < 0;
}

int Point::getX() const {
	return x;
}

int Point::getY() const {
	return y;
}

PointList Point::getSurrounds(int boardSize) const {
	PointList surr;
	for (int i = -1; i <= 1; ++i) {
		for (int j = -1; j <= 1; ++j) {
			Point tmpP(x + i, y + j);
			if (i != j && i != -j && !tmpP.isBad(boardSize) && !equals(tmpP)) {
				surr.push_back(tmpP);
			}
		}
	}
	return surr;
}
