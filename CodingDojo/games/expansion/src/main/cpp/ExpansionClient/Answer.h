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
#include "Point.h"
#include "Direction.h"

struct AddForces
{
	unsigned size;
	Point pt;
};

struct MoveForces
{
	unsigned size;
	Point from;
	Direction direction;
};

class AnswerBuilder
{
public:
	void AddAddForces(AddForces el) { addForcesElements.push_back(el); }
	void AddMoveForces(MoveForces el) { moveForcesElements.push_back(el); }
	std::string build(void);

private:
	static std::string tune(const std::string &json);

private:
	std::vector<AddForces> addForcesElements;
	std::vector<MoveForces> moveForcesElements;
};

std::string doNothingAnswer(void);
