package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.snake.model.Elements;
import com.codenjoy.dojo.snake.model.Hero;

public class Wall extends PointImpl implements Element, State<Elements, Object> {

	public Wall(Point point) {
		super(point);
	}

    public Wall(int x, int y) {
        super(x, y);
    }

	@Override
	public void affect(Hero snake) {
		snake.killMe();
	}

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        return Elements.BREAK;
    }
}
