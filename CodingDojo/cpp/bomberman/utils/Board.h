#ifndef BOARD_H
#define BOARD_H

#include "utils.h"

#include "Point.h"
#include "LengthToXY.h"
#include "../Element.h"

class Board {
public:
	Board(String boardString = LL(""));

	Element getAt(int x, int y) const;
	bool isAt(int x, int y, Element el) const;
	bool isAt(int x, int y, std::list<Element> els) const;

	Point getBomberman() const;
	PointList getOtherBombermans() const;
	bool isMyBombermanDead() const;

	int boardSize() const;

	PointList getBarriers() const;
	String toString() const;

	PointList getMeatChoppers() const;

	PointList getWalls() const;
	PointList getDestoyWalls() const;
	PointList getBombs() const;
	PointList getBlasts() const;

	PointList getFutureBlasts() const;

	bool isNear(int x, int y, Element el) const;
	int countNear(int x, int y, Element el) const;

	bool isBarrierAt(int x, int y) const;
private:
	PointList findAll(Element el) const;
	PointList removeDuplicates(PointList lst) const;
	String boardAsString() const;

	String board;
	int size;
	LengthToXY xyl;
};

#endif