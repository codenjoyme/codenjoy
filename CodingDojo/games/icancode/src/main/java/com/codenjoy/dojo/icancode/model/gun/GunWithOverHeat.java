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

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;

public class GunWithOverHeat extends GunWithRecharge {

    private boolean cool;
    private int heat;
    private int coolDown;

    public GunWithOverHeat() {
        super();
    }

    @Override
    public void reset() {
        super.reset();
        cool = true;
        heat = 0;
        coolDown = 0;
    }

    @Override
    public void shoot() {
        super.shoot();
        coolDown = 0;
        heat++;
    }

    @Override
    public void unlimitedShoot() {
        shoot();
        heat = 0;
    }

    @Override
    public void tick() {
        super.tick();
        if (heat == 0 || settings.integer(GUN_REST_TIME) == 0) {
            cool = true;
            return;
        }
        if (heat == settings.integer(GUN_SHOT_QUEUE) && cool) {
            cool = false;
            heat = settings.integer(GUN_REST_TIME);
        }
        if (cool) {
            int charge = settings.integer(GUN_RECHARGE) + 1;
            if (coolDown == charge) {
                coolDown = 0;
                heat--;
            } else {
                coolDown++;
            }
        } else {
            heat--;
        }
    }

    @Override
    public boolean canShoot() {
        return cool && super.canShoot();
    }
}
