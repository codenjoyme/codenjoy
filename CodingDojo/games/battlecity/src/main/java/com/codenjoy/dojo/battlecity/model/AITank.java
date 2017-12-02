package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

public class AITank extends Tank {

    private int act;

    public AITank(int x, int y, Dice dice, Direction direction) {
        super(x, y, direction, dice, 1);
    }

    @Override
    public void move() {
        if (act++ % 10 == 0) {
            act();
        }

        int c = 0;
        int x = 0;
        int y = 0;
        do {
            x = direction.changeX(getX());
            y = direction.changeY(getY());

            if (field.isBarrier(x, y)) {
                direction = Direction.random();
            }
        } while (field.isBarrier(x, y) && c++ < 10);

        moving = true;

        super.move();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (isAlive()) {
            switch (direction) {
                case LEFT:  return Elements.AI_TANK_LEFT;
                case RIGHT: return Elements.AI_TANK_RIGHT;
                case UP:    return Elements.AI_TANK_UP;
                case DOWN:  return Elements.AI_TANK_DOWN;
                default: throw new RuntimeException("Неправильное состояние танка!");
            }
        } else {
            return Elements.BANG;
        }
    }
}
