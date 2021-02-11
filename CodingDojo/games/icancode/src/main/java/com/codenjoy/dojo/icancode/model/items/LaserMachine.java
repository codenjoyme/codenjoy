package com.codenjoy.dojo.icancode.model.items;

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


import com.codenjoy.dojo.icancode.model.FieldItem;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.Player;

public class LaserMachine extends FieldItem implements Tickable {

    private static final int CHARGED = 6;
    private final Direction direction;
    private int timer;

    public LaserMachine(Elements element) {
        super(element, IMPASSABLE);
        timer = 0;
        this.direction = getDirection(element);
    }

    private Direction getDirection(Elements element) {
        switch (element) {
            case LASER_MACHINE_CHARGING_LEFT: return Direction.LEFT;
            case LASER_MACHINE_CHARGING_RIGHT: return Direction.RIGHT;
            case LASER_MACHINE_CHARGING_UP: return Direction.UP;
            case LASER_MACHINE_CHARGING_DOWN: return Direction.DOWN;
            default: throw new IllegalStateException("Unexpected element: " + element);
        }
    }

    private Elements getChargedElement(Direction direction) {
        switch (direction) {
            case LEFT: return Elements.LASER_MACHINE_READY_LEFT;
            case RIGHT: return Elements.LASER_MACHINE_READY_RIGHT;
            case UP: return Elements.LASER_MACHINE_READY_UP;
            case DOWN: return Elements.LASER_MACHINE_READY_DOWN;
            default: throw new IllegalStateException("Unexpected direction: " + direction);
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (timer == CHARGED) {
            return getChargedElement(direction);
        } else {
            return super.state(player, alsoAtPoint);
        }
    }

    @Override
    public void tick() {
        if (timer == CHARGED) {
            timer = 0;

            field.fire(this, direction, getCell());
        } else {
            timer++;
        }
    }

    public void reset() {
        timer = 0;
        field.lasers().forEach(it -> it.die());
    }
}
