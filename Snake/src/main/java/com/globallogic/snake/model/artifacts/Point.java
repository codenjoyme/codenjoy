package com.globallogic.snake.model.artifacts;

public class Point {

	private int x;
	private int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(Point point) {
		this(point.x, point.y);
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
