package com.globallogic.snake;

public class Snake {

	private int x;
	private int y;

	public Snake(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getLength() {
		return 2;
	}

	public String getDirection() {
		return "right";
	}

	public void moveLeft() {
		x++;
	}

}
