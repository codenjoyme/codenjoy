package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class Apple extends Point implements Element {

	public Apple(int x, int y) {
		super(x, y);
	}

	public boolean itsMe(Point point) {
		return x == point.x && y == point.y;
	}

	@Override
	public void modify(Snake snake) {
		snake.grow();
	}

}
