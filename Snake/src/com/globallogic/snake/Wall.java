package com.globallogic.snake;

public class Wall extends Point implements Element {

	public Wall(Point point) {
		super(point);
	}

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
