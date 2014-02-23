#ifndef YOURDIRECTIONSOLVER_H
#define YOURDIRECTIONSOLVER_H

#include "utils/utils.h"

#include "DirectionSolver.h"
#include "Direction.h"
#include "Element.h"
#include "utils/Dice.h"
#include "utils/Point.h"

class YourDirectionSolver :	public DirectionSolver
{
public:
	YourDirectionSolver(Dice* d) : dice(d) {}

	// From DirectionSolver
	virtual String get(Board board);

private:
	Dice* dice;
};

#endif