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
