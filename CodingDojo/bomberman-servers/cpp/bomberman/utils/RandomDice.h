#ifndef RANDOMDICE_H
#define RANDOMDICE_H

#include "utils.h"
#include "Dice.h"
#include <random>

class RandomDice : public Dice {
public:
	RandomDice();
	virtual int next(int max);
private:
	std::default_random_engine generator;
};

#endif