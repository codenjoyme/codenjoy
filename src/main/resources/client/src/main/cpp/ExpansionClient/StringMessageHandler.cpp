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
