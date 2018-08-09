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
#include "Direction.h"


Direction::Direction(int value) {
	auto i = Directions.find(valueOf(value));
	if (i == Directions.end()) {
		throw std::invalid_argument("Direction::Direction(int value): No direction for " + value);
	}
	dir = *i;
}

Direction::Direction(String name) {
	auto i = Directions.find(name);
	if (i == Directions.end()) {
		throw std::invalid_argument("Direction::Direction(String name): No direction for such name ");
	}
	dir = *i;
}

bool Direction::operator==(const Direction& d) const {
	return d.dir == dir;
}

bool Direction::operator!=(const Direction& d) const {
	return !operator==(d);
}

bool Direction::isNull() const {
	return dir.first == LL("NULL");
}

String Direction::toString() const {
	StringStream ss;
	ss <<  (isNull() ? LL("") : dir.first);
	return ss.str();
}

String Direction::valueOf(int value) {
	for (auto i : Directions) {
		if (std::get<0>(i.second) == value) return i.first;
	}
	throw std::invalid_argument("Direction::valueOf(int value): No direction for " + value);
}

int Direction::changeX(int x) const {
	return x + std::get<1>(dir.second);
}

int Direction::changeY(int y) const {
	return y - std::get<2>(dir.second);
}

Direction Direction::inverted() const {
	if (dir.first == LL("UP")) return Direction(LL("DOWN"));
	if (dir.first == LL("DOWN")) return Direction(LL("UP"));
	if (dir.first == LL("LEFT")) return Direction(LL("RIGHT"));
	if (dir.first == LL("RIGHT")) return Direction(LL("LEFT"));
	return Direction(LL("STOP"));
}


DirectionMap Direction::initialise() {
	DirectionMap mapOfDirections;
	mapOfDirections[LL("NULL")] = DirectionVector(-1, 0, 0);

	// direction of Bomberman
	mapOfDirections[LL("UP")] = DirectionVector(2, 0, -1);
	mapOfDirections[LL("DOWN")] = DirectionVector(3, 0, 1);
	mapOfDirections[LL("LEFT")] = DirectionVector(0, -1, 0);
	mapOfDirections[LL("RIGHT")] = DirectionVector(1, 1, 0);

	// drop a bomb
	mapOfDirections[LL("ACT")] = DirectionVector(4, 0, 0);

	// stop the Bomberman
	mapOfDirections[LL("STOP")] = DirectionVector(5, 0, 0);
	return mapOfDirections;
}

const DirectionMap Direction::Directions = Direction::initialise();
