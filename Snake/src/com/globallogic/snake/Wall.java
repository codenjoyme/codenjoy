package com.globallogic.snake;

public class Wall implements Element {

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
