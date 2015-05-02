package com.codenjoy.dojo.services;

public class PlayerSpy {
    private int ticksWithoutAct;

    public void act() {
        ticksWithoutAct = 0;
    }

    public void tick() {
        ticksWithoutAct++;
    }

    public boolean playing(int ticks) {
        return ticksWithoutAct < ticks;
    }
}
