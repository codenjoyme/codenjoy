package com.globallogic.snake;


public class Stone implements Element {

	private int x;
	private int y;

	public Stone(int x, int y) {
		this.x = x; 
		this.y = y;
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return String.format("Позиция камня x:%s, y:%s", x, y);
	}

	public boolean itsMe(int x2, int y2) {
		return x == x2 && y == y2;
	}

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
