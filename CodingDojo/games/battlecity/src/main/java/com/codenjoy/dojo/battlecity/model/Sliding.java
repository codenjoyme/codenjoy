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
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.SLIPPERINESS;

public class Sliding {

    private Field field;
    private int tick;
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

    public Direction affect(Direction current) {
        if (tick == slipperiness()) {
            tick = 0;
            before = current;
        } else {
            // ignore current direction because sliding
        }

        tick++;

        return before;
    }

    public boolean active(Tank tank) {
        return field.isIce(tank)
                && slipperiness() != 0
                && !tank.prizes().contains(Elements.PRIZE_NO_SLIDING);
    }

    public void stop() {
        tick = slipperiness();
    }
}
