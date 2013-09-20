#include "Point.h"


Point::Point(int _x, int _y) : x(_x), y(_y), isSetAsNull(false) {
}

bool Point::operator==(const Point& pt) const {
	return equals(pt);
}

bool Point::equals(const Point& pt) const {
	return x == pt.x && y == pt.y;
}

bool Point::isNull() const {
	return isSetAsNull;
}

void Point::setNull(bool state) {
	isSetAsNull = state;
}

String Point::toString() const {
	StringStream ss;
	ss << "[" << x << "," << y << "]";
	return ss.str();
}

bool Point::isBad(int boardSize) const {
	return x >= boardSize || y >= boardSize || x < 0 || y < 0;
}

int Point::getX() const {
	return x;
}

int Point::getY() const {
	return y;
}

PointList Point::getSurrounds(int boardSize) const {
	PointList surr;
	for (int i = -1; i <= 1; ++i) {
		for (int j = -1; j <= 1; ++j) {
			Point tmpP(x + i, y + j);
			if (i != j && i != -j && !tmpP.isBad(boardSize) && !equals(tmpP)) {
				surr.push_back(tmpP);
			}
		}
	}
	return surr;
}