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

struct Point
{
	int x;
	int y;
};

inline bool operator<(const Point& l, const Point& r)
{
	if (l.x < r.x)
		return true;

	if (l.x != r.x)
		return false;

	return l.y < r.y;
}

inline bool operator==(const Point& l, const Point& r)
{
	return (l.x == r.x) && (l.y == r.y);
}

inline bool operator!=(const Point& l, const Point& r)
{
	return (l.x != r.x) || (l.y != r.y);
}
