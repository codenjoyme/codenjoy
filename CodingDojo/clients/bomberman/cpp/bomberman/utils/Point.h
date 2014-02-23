#ifndef POINT_H
#define POINT_H

#include "utils.h"

class Point;
typedef std::list<Point> PointList;

class Point {
public:
	Point(int _x = 0, int _y = 0);

	bool equals(const Point& pt) const;
	bool operator==(const Point& pt) const;

	String toString() const;

	bool isBad(int boardSize) const;
	bool isNull() const;
	void setNull(bool nullState);
	int getX() const;
	int getY() const;
	PointList getSurrounds(int boardSize) const;
private:
	int x;
	int y;
	bool isSetAsNull;
};

#endif 