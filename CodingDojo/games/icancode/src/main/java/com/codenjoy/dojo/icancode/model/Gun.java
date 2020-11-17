package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.services.Tickable;

public class Gun implements Tickable {
    private boolean canFire;
    private int recharge;
    private int ticks;

    public Gun(int recharge) {
        this.recharge = recharge;
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
        if (recharge <= 0) {
            canFire = true;
        } else if (ticks == recharge) {
            reset();
        }
    }

    public boolean tryToFire() {
        boolean result = canFire;
        canFire = false;
        return result;
    }
}
