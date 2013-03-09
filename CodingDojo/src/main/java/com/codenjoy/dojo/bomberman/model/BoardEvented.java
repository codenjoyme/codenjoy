package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.EventListener;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 8:23 PM
 */
public class BoardEvented extends Board {
    private EventListener listener;

    public BoardEvented(Walls walls, Level level, int size, EventListener listener) {
        super(walls, level, size);
        this.bomberman = new BombermanEvented(level, this, listener);
        this.listener = listener;
    }

    protected Wall destroy(Point blast) {
        Wall destroy = super.destroy(blast);
        if (destroy instanceof MeatChopper) {
            listener.event(BombermanEvents.KILL_MEAT_CHOPPER.name());
        } else if (destroy instanceof DestroyWall) {
            listener.event(BombermanEvents.KILL_DESTROY_WALL.name());
        }
        return destroy;
    }

    public EventListener getEventListener() {
        return listener;
    }

}
