#include "LengthToXY.h"


LengthToXY::LengthToXY(int _boardSize) : boardSize(_boardSize) {
}

Point LengthToXY::getXY(int length) const {
	if (length < 0) {
		throw std::invalid_argument("LengthToXY::getXY(int length): length cant be <= 0!");
	}
	return Point(length % boardSize, length / boardSize);
}

int LengthToXY::getLength(int _x, int _y) const {
	return _y * boardSize + _x;
}