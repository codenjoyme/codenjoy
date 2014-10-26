package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.snake.model.Hero;

public class Apple extends EateablePoint implements Element {

	public Apple(int x, int y) {
		super(x, y);
	}

	@Override
     public void affect(Hero snake) {
        snake.grow();
        super.affect(snake);
    }

}
