package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class EmptySpace extends Point implements Element {

	public EmptySpace(Point point) {
		super(point);
	}

	@Override
	public void modify(Snake snake) {
		// do nothing		
	}

}
