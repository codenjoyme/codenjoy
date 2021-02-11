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
#include "StringMessagesHandler.h"
#include "Answer.h"
#include "InputMessage.h"


std::string StringMessageHandler::Handle(const std::string& message, bool& finish)
{
	--testCountdown;
	finish = testCountdown <= 0;

	InputMessage iMsg(message);

	AnswerBuilder answerBuilder;
	answerBuilder.AddAddForces(AddForces{ iMsg.getAvailableForces(),  iMsg.getMyBaseLocation() });
	answerBuilder.AddMoveForces(MoveForces{ iMsg.getForcesSize(iMsg.getMyBaseLocation()), iMsg.getMyBaseLocation(), Direction::RIGHT });

	return answerBuilder.build();
}
