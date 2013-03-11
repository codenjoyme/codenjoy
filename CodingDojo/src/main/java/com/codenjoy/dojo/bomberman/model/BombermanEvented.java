package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.EventListener;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 8:34 PM
 */
public class BombermanEvented extends MyBomberman implements Bomberman {
    private EventListener listener;

    public BombermanEvented(Level level, EventListener listener) {
        super(level);
        this.listener = listener;
    }

    @Override
    public void kill() {
        super.kill();
        listener.event(BombermanEvents.KILL_BOMBERMAN.name());
    }
}
