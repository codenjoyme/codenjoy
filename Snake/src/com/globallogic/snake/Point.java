package com.globallogic.snake;

public class Point {

	int x;
	int y;

	public Point(int x2, int y2) {
		x = x2;
		y = y2;
	}

	public boolean itsMe(Point point) {
		return x == point.x && y == point.y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return String.format("[%s:%s]", x, y);
	}
}
