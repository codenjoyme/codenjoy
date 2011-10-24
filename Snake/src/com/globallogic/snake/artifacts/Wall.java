package com.globallogic.snake.artifacts;

import com.globallogic.snake.Snake;

public class Wall extends Point implements Element {

	public Wall(Point point) {
		super(point);
	}

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
