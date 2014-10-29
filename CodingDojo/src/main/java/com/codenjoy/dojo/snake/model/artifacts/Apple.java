package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.snake.model.Elements;
import com.codenjoy.dojo.snake.model.Hero;

public class Apple extends EateablePoint implements Element, State<Elements, Object> {

	public Apple(int x, int y) {
		super(x, y);
	}

	@Override
     public void affect(Hero snake) {
        snake.grow();
        super.affect(snake);
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        return Elements.GOOD_APPLE;
    }
}
