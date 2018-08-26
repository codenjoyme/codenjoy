package com.codenjoy.dojo.services.nullobj;

import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class NullPlayerHero extends PlayerHero {

    public static final PlayerHero INSTANCE = new NullPlayerHero();

    @Override
    public void down() {
        // do nothing
    }

    @Override
    public void up() {
        // do nothing
    }

    @Override
    public void left() {
        // do nothing
    }

    @Override
    public void right() {
        // do nothing
    }

    @Override
    public void act(int... p) {
        // do nothing
    }

    @Override
    public void tick() {
        // do nothing
    }
}
