package com.globallogic.snake.artifacts;

import com.globallogic.snake.Snake;

public class EmptySpace extends Point implements Element {

	public EmptySpace(Point point) {
		super(point);
	}

	@Override
	public void modify(Snake snake) {
		// do nothing		
	}

}
