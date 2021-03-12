package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.settings.SettingsReader;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.PRIZE_WORKING;
import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.SLIPPERINESS;

public class Sliding {

    private Field field;
    private int tick;
    private int tickTankOnIceWithPrize;
    private Direction before;

    private GameSettings settings;

    public Sliding(Field field, Direction before, GameSettings settings) {
        this.field = field;
        this.before = before;
        this.settings = settings;
    }

    private int slipperiness() {
        return settings.integer(SLIPPERINESS);
    }

    private int prizeWorking() {
        return settings.integer(PRIZE_WORKING);
    }

    public Direction act(Tank tank) {

        if (!field.isIce(tank) || slipperiness() == 0) {
            tick = 0;
            return before = tank.getDirection();
        }

        if (tickTankOnIceWithPrize == prizeWorking()) {
            tickTankOnIceWithPrize = 0;
            return before;
        }

        if (tick == slipperiness()) {
            tick = 0;
            before = tank.getDirection();
        } else {
            // ignore current direction because sliding
        }

        tick++;
        tickTankOnIceWithPrize++;

        return before;
    }

    public void stop() {
        tick = slipperiness();
    }
}
