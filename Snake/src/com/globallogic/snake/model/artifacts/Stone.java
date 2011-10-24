package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;


public class Stone extends Point implements Element {
	
	public Stone(int x, int y) {
		super(x, y);
	}

	@Override
	public String toString() {
		return String.format("Позиция камня x:%s, y:%s", getX(), getY());
	}

	@Override
	public void modify(Snake snake) {
		snake.killMe();
	}

}
