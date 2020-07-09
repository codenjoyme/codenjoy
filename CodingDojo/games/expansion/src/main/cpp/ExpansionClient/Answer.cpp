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
