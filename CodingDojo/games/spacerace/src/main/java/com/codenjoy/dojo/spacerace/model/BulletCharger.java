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
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.BULLETS_COUNT;
import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.TICKS_TO_RECHARGE;

public class BulletCharger implements Tickable {

    private int timer = 0;
    private int bullets = 0;
    private boolean toRecharge = false;
    private SettingsReader<?> settings;

    public BulletCharger(SettingsReader<?> settings) {
        this.settings = settings;
    }

    public void setToRecharge(boolean toRecharge) {
        this.toRecharge = toRecharge;
    }

    @Override
    public void tick() {
        if (toRecharge) {
            rechargeFull();
            toRecharge = false;
        }
        int ticksToRecharge = ticksToRecharge();
        if (ticksToRecharge > 0) {
            timer++;
            if (timer > ticksToRecharge) {
                rechargeOne();
                timer = 0;
            }
        }
    }

    private void rechargeFull() {
        bullets = maxBulletsCount();
    }

    private void rechargeOne() {
        if (bullets < maxBulletsCount()) {
            bullets++;
        }
    }

    public boolean canShoot() {
        boolean result = bullets > 0;
        if (result) {
            bullets--;
        }
        return result;
    }

    public int ticksToRecharge() {
        return settings.integer(TICKS_TO_RECHARGE);
    }

    public int maxBulletsCount() {
        return settings.integer(BULLETS_COUNT);
    }
}
