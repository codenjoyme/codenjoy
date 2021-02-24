package com.codenjoy.dojo.battlecity.model;

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


import com.codenjoy.dojo.battlecity.services.GameSettings;
import com.codenjoy.dojo.services.Tickable;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.TANK_TICKS_PER_SHOOT;

public class Gun implements Tickable {

    private boolean canFire;
    private boolean machineGun;
    private int ticks;
    private int shootThrough;
    private GameSettings settings;

    public Gun(GameSettings settings) {
        this.settings = settings;
        reset();
    }

    private int ticksPerShoot() {
        return settings.integer(TANK_TICKS_PER_SHOOT);
    }

    public void reset() {
        ticks = 0;
        canFire = true;
        machineGun = false;
    }

    @Override
    public void tick() {
        selectMode();

        if (!canFire) {
            ticks++;
        }
        if (shootThrough <= 0) {
            canFire = true;
            machineGun = false;
        } else if (ticks == shootThrough) {
            reset();
        }
    }

    public boolean tryToFire() {
        boolean result = canFire;
        canFire = false;
        return result;
    }


    public void machineGun() {
        machineGun = true;
    }

    private void selectMode() {
        if (machineGun) {
            shootThrough = 0;
        } else {
            shootThrough = ticksPerShoot();
        }
    }
}
