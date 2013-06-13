package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.Snake;

public class Wall extends PointImpl implements Element {

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
