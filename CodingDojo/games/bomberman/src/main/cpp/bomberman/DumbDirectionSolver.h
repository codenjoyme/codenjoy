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
#ifndef DUMBDIRECTIONSOLVER_H
#define DUMBDIRECTIONSOLVER_H

#include "utils/utils.h"

#include "DirectionSolver.h"
#include "Direction.h"
#include "Element.h"
#include "utils/Dice.h"
#include "utils/Point.h"

class DumbDirectionSolver :	public DirectionSolver
{
public:
	DumbDirectionSolver(Dice* d) : dice(d) {}

	// From DirectionSolver
	virtual String get(Board board);

private:
	String mergeCommands(Point bomb, Direction direction) const;
	Direction tryToMove(Point pt);
	Direction whereICanGoFrom(Point pt);

	Direction direction;
	Point bomb;
	Dice* dice;
	Board board;
};

#endif
