package com.globallogic.snake;

public class Apple implements Element {

	private int x;
	private int y;

	public Apple(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() { 
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean itsMe(int x2, int y2) {
		return x == x2 && y == y2;
	}

	@Override
	public void modify(Snake snake) {
		snake.grow();
	}

}
