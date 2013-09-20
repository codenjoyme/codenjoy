#include "DumbDirectionSolver.h"


String DumbDirectionSolver::get(Board b) {
	board = b;
	Point bomberman = board.getBomberman();

    bool nearDestroyWall = board.isNear(bomberman.getX(), bomberman.getY(), Element(LL("DESTROY_WALL")));
    bool nearBomberman = board.isNear(bomberman.getX(), bomberman.getY(), Element(LL("OTHER_BOMBERMAN")));
    bool nearMeatchopper = board.isNear(bomberman.getX(), bomberman.getY(), Element(LL("MEAT_CHOPPER")));
    bool bombNotDropped = !board.isAt(bomberman.getX(), bomberman.getY(), Element(LL("BOMB_BOMBERMAN")));

	bomb.setNull(true);
    if ((nearDestroyWall || nearBomberman || nearMeatchopper) && bombNotDropped) {
        bomb = bomberman;
		bomb.setNull(false);
    }

    direction = tryToMove(bomberman);

    return mergeCommands(bomb, direction);
}

String DumbDirectionSolver::mergeCommands(Point bomb, Direction direction) const {
	if (direction == Direction(LL("STOP"))) {
		bomb.setNull(true);
	}
	StringStream ss;
	if (!bomb.isNull()) {
		ss << Direction(LL("ACT")).toString();
		if (direction != Direction(LL("NULL"))) {
			ss << LL(",");
		}
	}
	ss << direction.toString();
	return ss.str();
}

Direction DumbDirectionSolver::tryToMove(Point pt) {
	int count = 0;
	int newX = pt.getX();
	int newY = pt.getY();
	Direction result(LL("NULL"));
	bool again = false;
	do {
			result = whereICanGoFrom(pt);
			if (result.isNull()) {
				return result;
			}

			newX = result.changeX(pt.getX());
			newY = result.changeY(pt.getY());

			bool bombAtWay = (!bomb.isNull()) && (bomb == Point(newX, newY));
			bool barrierAtWay = board.isBarrierAt(newX, newY);
			auto futBla = board.getFutureBlasts();
			bool blastAtWay = (std::find(futBla.begin(), futBla.end(), Point(newX, newY)) != futBla.end());
			bool meatChopperNearWay = board.isNear(newX, newY, Element(LL("MEAT_CHOPPER")));

			if (blastAtWay &&
				board.countNear(pt.getX(), pt.getY(), Element(LL("SPACE"))) == 1 &&
				!board.isAt(pt.getX(), pt.getY(), Element(LL("BOMB_BOMBERMAN")))) {
					result = Direction(LL("STOP"));
					return result;
			}

			again = bombAtWay || barrierAtWay || meatChopperNearWay;

			bool deadEndAtWay = (board.countNear(newX, newY, Element(LL("SPACE"))) == 0 && !bomb.isNull());
			if (deadEndAtWay) {
				bomb.setNull(true);
			}
			if (result == Direction(LL("NULL"))) {
				again == true;
			}
	} while (count++ < 20 && again);
	if (count >= 20) {
		result = Direction(LL("ACT"));
    }
	return result;
}

Direction DumbDirectionSolver::whereICanGoFrom(Point pt) {
	Direction result;
	int count = 0;
	result = Direction(Direction::valueOf(dice->next(3)));
	while (count++ < 10 &&
			(
				(result.inverted() == direction && bomb.isNull()) ||
				!board.isAt(result.changeX(pt.getX()), result.changeY(pt.getY()), Element(LL("SPACE")))
			)
		   )  {
		result = Direction(Direction::valueOf(dice->next(3)));
	}

	if (count > 10) {
		result = Direction(LL("NULL"));
    }
    return result;
}