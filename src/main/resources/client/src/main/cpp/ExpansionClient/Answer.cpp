#include "stdafx.h"
#include "Answer.h"
#include "cpprest/json.h"


std::string AnswerBuilder::tune(const std::string &json)
{
	return "message('" + json + "')";
}

std::string AnswerBuilder::build(void)
{
	web::json::value jsonAddForces = web::json::value::array(addForcesElements.size());
	for (size_t i = 0; i < addForcesElements.size(); ++i)
	{
		web::json::value pt;
		pt[L"x"] = addForcesElements[i].pt.x;
		pt[L"y"] = addForcesElements[i].pt.y;
		web::json::value el;
		el[L"count"] = addForcesElements[i].size;
		el[L"region"] = pt;

		jsonAddForces[i] = el;
	}

	web::json::value jsonMoveForces = web::json::value::array(moveForcesElements.size());
	for (size_t i = 0; i < moveForcesElements.size(); ++i)
	{
		web::json::value pt;
		pt[L"x"] = moveForcesElements[i].from.x;
		pt[L"y"] = moveForcesElements[i].from.y;
		web::json::value el;
		el[L"count"] = moveForcesElements[i].size;
		el[L"region"] = pt;
		el[L"direction"] = web::json::value(getDirectionName(moveForcesElements[i].direction));

		jsonMoveForces[i] = el;
	}

	web::json::value answer;
	answer[L"movements"] = jsonMoveForces;
	answer[L"increase"] = jsonAddForces;

	return tune(utility::conversions::to_utf8string(answer.serialize()));
}

std::string doNothingAnswer(void)
{
	AnswerBuilder ab;
	return ab.build();
}
