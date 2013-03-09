package com.codenjoy.dojo.snake.model.middle;

import com.codenjoy.dojo.snake.model.Joystick;
import com.codenjoy.dojo.snake.model.Snake;

public class SnakeEvented extends Snake implements Joystick {

    private EventListener listener;

    public SnakeEvented(EventListener listener, int x, int y) {
        super(x, y);
        this.listener = listener;
	}

	public void killMe() {
		listener.event(SnakeEvents.KILL.name());
        super.killMe();
	}

	public void grow() {
        listener.event(SnakeEvents.EAT_APPLE.name());
        super.grow();
	}

	public void eatStone() {
        listener.event(SnakeEvents.EAT_STONE.name());
        super.eatStone();
	}	

}
