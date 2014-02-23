#include "RandomDice.h"

RandomDice::RandomDice() {
}

int RandomDice::next(int max) {
	std::uniform_int_distribution<int> distr(0, max);
	return distr(generator);
}