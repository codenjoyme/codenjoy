package com.codenjoy.dojo.snake.model.middle;

import com.codenjoy.dojo.snake.model.Joystick;
import com.codenjoy.dojo.snake.model.Snake;

public class SnakeEvented extends Snake implements Joystick {

    private SnakeEventListener listener;

    public SnakeEvented(SnakeEventListener listener, int x, int y) {
        super(x, y);
        this.listener = listener;
	}

	public void killMe() {
		listener.snakeIsDead();
        super.killMe();
	}

	public void grow() {
        listener.snakeEatApple();
        super.grow();
	}

	public void eatStone() {
        listener.snakeEatStone();
        super.eatStone();
	}	

}
