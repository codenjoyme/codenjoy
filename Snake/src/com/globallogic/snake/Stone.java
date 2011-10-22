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

	public boolean itsMe(Point point) {
		return x == point.x && y == point.y;
	}

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
