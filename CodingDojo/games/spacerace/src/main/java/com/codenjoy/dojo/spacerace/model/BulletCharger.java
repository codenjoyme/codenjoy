package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Tickable;

/**
 * Created by indigo on 08.08.2015.
 */
public class BulletCharger implements Tickable{
    private final int ticksToRecharge;
    private final int bulletsCount;
    private int timer = 0;
    private int bullets = 0;

    public BulletCharger(int ticksToRecharge, int bulletsCount) {
        this.ticksToRecharge = ticksToRecharge;
        this.bulletsCount = bulletsCount;
    }

    @Override
    public void tick() {
        if (timer == 0) {
            recharge();
        }
        timer--;
    }

    private void recharge() {
        timer = ticksToRecharge;
        bullets = bulletsCount;
    }


    public boolean canShoot() {
        boolean result = bullets > 0;
        if (result) {
            bullets--;
        }
        return result;
    }

    public int getTicksToRecharge() {
        return ticksToRecharge;
    }

    public int getBulletsCount() {
        return bulletsCount;
    }
}
