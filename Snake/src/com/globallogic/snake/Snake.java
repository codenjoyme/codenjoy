package com.globallogic.snake;

public class Snake {

	private int positionX;
	private int positionY;

	public Snake(int positionX, int positionY) {
		this.positionX = positionX;
		this.positionY = positionY;
	}

	public int getX() {
		return positionX;
	}

	public int getY() {
		return positionY;
	}

	public int getLength() {
		return 2;
	}

	public String getDirection() {
		return "right";
	}

}
