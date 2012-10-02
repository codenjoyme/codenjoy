package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class Apple extends EateablePoint implements Element {

	public Apple(int x, int y) {
		super(x, y);
	}

	@Override
     public void affect(Snake snake) {
        snake.grow();
        super.affect(snake);
    }

}
