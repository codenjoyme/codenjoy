package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Tickable;

/**
 * User: sanja
 * Date: 14.12.13
 * Time: 8:05
 */
public class Gun implements Tickable {
    private boolean canFire;
    private int ticksPerBullets;
    private int ticks;

    public Gun(int ticksPerBullets) {
        this.ticksPerBullets = ticksPerBullets;
        ticks = 0;
        canFire = true;
    }

    @Override
    public void tick() {
        ticks++;
        if (ticksPerBullets <= 0) {
            canFire = true;
        }
        if (ticks == ticksPerBullets) {
            canFire = true;
            ticks = 0;
        }
    }

    public boolean tryToFire() {
        boolean result = canFire;
        canFire = false;
        return result;
    }
}
