package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.services.Tickable;

public class Gun implements Tickable {
    private boolean canFire;
    private int ticksPerBullets;
    private int ticks;

    public Gun(int ticksPerBullets) {
        this.ticksPerBullets = ticksPerBullets;
        reset();
    }

    public void reset() {
        ticks = 0;
        canFire = true;
    }

    @Override
    public void tick() {
        if (!canFire) {
            ticks++;
        }
        if (ticksPerBullets <= 0) {
            canFire = true;
        } else if (ticks == ticksPerBullets) {
            reset();
        }
    }

    public boolean tryToFire() {
        boolean result = canFire;
        canFire = false;
        return result;
    }
}
