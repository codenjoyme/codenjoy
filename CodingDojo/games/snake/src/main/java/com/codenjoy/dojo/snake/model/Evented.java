package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.snake.services.Events;

public class Evented extends Hero implements Joystick {

    private EventListener listener;

    public Evented(EventListener listener, int x, int y) {
        super(x, y);
        this.listener = listener;
	}

	public void killMe() {
		listener.event(Events.KILL);
        super.killMe();
	}

	public void grow() {
        listener.event(Events.EAT_APPLE);
        super.grow();
	}

	public void eatStone() {
        listener.event(Events.EAT_STONE);
        super.eatStone();
	}	

}
