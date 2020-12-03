package com.codenjoy.dojo.icancode.model.gun;

public class GunWithRecharge implements Gun {

    protected boolean canShoot;
    protected int ticks;

    public GunWithRecharge() {
        reset();
    }

    @Override
    public void reset() {
        recharge();
    }

    private void recharge() {
        ticks = 0;
        canShoot = true;
    }

    protected void discharge() {
        ticks = 0;
        canShoot = false;
    }

    @Override
    public void shoot() {
        discharge();
    }

    @Override
    public void unlimitedShoot() {
        shoot();
    }

    @Override
    public void tick() {
        if (!canShoot) {
            ticks++;
        }
        int chargePoints = getChargePoints();
        if (chargePoints <= 0) {
            canShoot = true;
        } else if (ticks == chargePoints + 1) {
            recharge();
        }
    }

    @Override
    public boolean isCanShoot() {
        return canShoot;
    }
}