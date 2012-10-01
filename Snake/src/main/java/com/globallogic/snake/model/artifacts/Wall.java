package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class Wall extends Point implements Element {

	public Wall(Point point) {
		super(point);
	}

    public Wall(int x, int y) {
        super(x, y);
    }

	@Override
	public void affect(Snake snake) {
		snake.killMe();
	}

}
