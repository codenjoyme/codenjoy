#ifndef DICE_H
#define DICE_H

#include "utils.h"

class Dice {
public:
	Dice() {};
	virtual int next(int max) = 0;
};

#endif