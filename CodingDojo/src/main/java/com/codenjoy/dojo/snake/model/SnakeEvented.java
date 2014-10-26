package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.snake.services.SnakeEvents;

public class SnakeEvented extends Hero implements Joystick {

    private EventListener listener;

    public SnakeEvented(EventListener listener, int x, int y) {
        super(x, y);
        this.listener = listener;
	}

	public void killMe() {
		listener.event(SnakeEvents.KILL);
        super.killMe();
	}

	public void grow() {
        listener.event(SnakeEvents.EAT_APPLE);
        super.grow();
	}

	public void eatStone() {
        listener.event(SnakeEvents.EAT_STONE);
        super.eatStone();
	}	

}
