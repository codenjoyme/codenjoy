package com.codenjoy.dojo.icancode.model.gun;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

// TODO refactoring needed
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
