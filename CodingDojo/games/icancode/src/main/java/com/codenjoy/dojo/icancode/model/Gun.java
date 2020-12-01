package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Tickable;

public class Gun implements Tickable {

    private boolean canFire;
    private int chargePoints;
    private int ticks;

    public Gun() {
        recharge();
    }

    public void recharge() {
        ticks = 0;
        canFire = true;
        chargePoints = SettingsWrapper.data.gunRecharge();
    }

    public void discharge() {
        ticks = 0;
        canFire = false;
    }

    @Override
    public void tick() {
        if (!canFire) {
            ticks++;
        }
        if (chargePoints <= 0) {
            canFire = true;
        } else if (ticks == chargePoints) {
            recharge();
        }
    }

    public boolean tryToFire() {
        boolean result = canFire;
        canFire = false;
        return result;
    }
}
