package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.Hero;

public class EmptySpace extends PointImpl implements Element {

	public EmptySpace(Point point) {
		super(point);
	}

	@Override
	public void affect(Hero snake) {
		// do nothing		
	}

}
