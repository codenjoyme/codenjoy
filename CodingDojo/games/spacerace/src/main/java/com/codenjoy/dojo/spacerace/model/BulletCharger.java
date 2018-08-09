package com.codenjoy.dojo.spacerace.model;

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


import com.codenjoy.dojo.services.Tickable;

public class BulletCharger implements Tickable {
    private final int ticksToRecharge;
    private final int bulletsCount;
    private int timer = 0;
    private int bullets = 0;
    private boolean toRecharge = false;

    public BulletCharger(int ticksToRecharge, int bulletsCount) {
        this.ticksToRecharge = ticksToRecharge;
        this.bulletsCount = bulletsCount;
    }

    public void setToRecharge(boolean toRecharge) {
        this.toRecharge = toRecharge;
    }

    @Override
    public void tick() {
        if (toRecharge) {
            recharge();
            toRecharge = false;
        }
        // if (timer == 0) { // TODO доделать, если нам понадобится перезарядка
        // по времени
        // recharge();
        // }
        // timer--;
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
