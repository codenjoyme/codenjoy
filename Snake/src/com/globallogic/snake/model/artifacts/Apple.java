package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class Apple extends Point implements Element {

	private Runnable doItOnEat;

	public Apple(int x, int y) {
		super(x, y);
	}

	@Override
	public void modify(Snake snake) { 
		snake.grow();
		if (doItOnEat != null) {
			doItOnEat.run();
		}
	}

	public void onEat(Runnable runnable) {
		this.doItOnEat = runnable;
	}

}
