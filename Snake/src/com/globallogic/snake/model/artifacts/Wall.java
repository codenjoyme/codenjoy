package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class Wall extends Point implements Element {

	public Wall(Point point) {
		super(point);
	}

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
