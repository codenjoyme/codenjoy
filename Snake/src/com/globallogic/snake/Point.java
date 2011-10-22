package com.globallogic.snake;

public class Point {

	int x;
	int y;

	public Point(int x2, int y2) {
		x = x2;
		y = y2;
	}

	public boolean itsMe(int x2, int y2) {
		return x == x2 && y == y2;
	}
	
	@Override
	public String toString() {
		return String.format("[%s:%s]", x, y);
	}
}
