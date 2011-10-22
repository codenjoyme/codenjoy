package com.globallogic.snake;


public class Stone extends Point implements Element {
	
	public Stone(int x, int y) {
		super(x, y);
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
