package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.snake.model.Elements;
import com.codenjoy.dojo.snake.model.Hero;


public class Stone extends EateablePoint implements Element, State<Elements, Object> {
	
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

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        return Elements.BAD_APPLE;
    }
}
