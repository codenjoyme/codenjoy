#pragma once

struct Point
{
	int x;
	int y;
};

inline bool operator<(const Point& l, const Point& r)
{
	if (l.x < r.x)
		return true;

	if (l.x != r.x)
		return false;

	return l.y < r.y;
}

inline bool operator==(const Point& l, const Point& r)
{
	return (l.x == r.x) && (l.y == r.y);
}

inline bool operator!=(const Point& l, const Point& r)
{
	return (l.x != r.x) || (l.y != r.y);
}
