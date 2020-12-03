package com.codenjoy.dojo.icancode.model.gun;

public class GunWithOverHeat extends GunWithRecharge {
    private boolean cool;
    private int heatPoints;
    private int coolDownPoints;


    @Override
    public void reset() {
        super.reset();
        cool = true;
        heatPoints = 0;
        coolDownPoints = 0;
    }

    @Override
    public void shoot() {
        super.shoot();
        coolDownPoints = 0;
        heatPoints++;
    }

    @Override
    public void unlimitedShoot() {
        shoot();
        heatPoints = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (heatPoints == 0 || getGunRestTime() == 0) {
            cool = true;
            return;
        }
        if (heatPoints == getGunShotQueue() && cool) {
            cool = false;
            heatPoints = getGunRestTime();
        }
        if (cool) {
            int chargePoints = getChargePoints() + 1;
            if (coolDownPoints == chargePoints) {
                coolDownPoints = 0;
                heatPoints--;
            } else {
                coolDownPoints++;
            }
        } else {
            heatPoints--;
        }
    }

    @Override
    public boolean isCanShoot() {
        return cool && super.isCanShoot();
    }
}
