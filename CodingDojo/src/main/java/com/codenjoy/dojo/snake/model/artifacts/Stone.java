package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.snake.model.Hero;


public class Stone extends EateablePoint implements Element {
	
	public Stone(int x, int y) {
		super(x, y);
	}

	@Override
	public String toString() {
		return String.format("Позиция камня x:%s, y:%s", getX(), getY());
	}

	@Override
	public void affect(Hero snake) {
		snake.eatStone();
        super.affect(snake);
	}
}
