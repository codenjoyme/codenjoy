package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.Snake;

public class EmptySpace extends PointImpl implements Element {

	public EmptySpace(Point point) {
		super(point);
	}

	@Override
	public void affect(Snake snake) {
		// do nothing		
	}

}
