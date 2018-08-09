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
#ifndef POINT_H
#define POINT_H

#include "utils.h"

class Point;
typedef std::list<Point> PointList;

class Point {
public:
	Point(int _x = 0, int _y = 0);

	bool equals(const Point& pt) const;
	bool operator==(const Point& pt) const;

	String toString() const;

	bool isBad(int boardSize) const;
	bool isNull() const;
	void setNull(bool nullState);
	int getX() const;
	int getY() const;
	PointList getSurrounds(int boardSize) const;
private:
	int x;
	int y;
	bool isSetAsNull;
};

#endif 
