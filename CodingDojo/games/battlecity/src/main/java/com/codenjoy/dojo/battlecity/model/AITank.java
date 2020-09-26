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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class AITank extends Tank {

    public static final int MAX = 10;
    public static final int ACT_COUNT = 10;
    private int act;
    private boolean noBulletFly = true;

    public AITank(Point pt, Dice dice, Direction direction) {
        super(pt, direction, dice, 1);
    }

    public AITank(Point pt, Dice dice, Direction direction, int ticksPerBullets, boolean noBulletFly) {
        super(pt, direction, dice, ticksPerBullets);
        this.noBulletFly = noBulletFly;
    }

    @Override
    public void move() {
        // TODO пока еще не понятно что тут делается
        if (noBulletFly && (act++ % ACT_COUNT == 0)) {
            act();
        }

        int c = 0;
        Point pt;
        do {
            pt = direction.change(this);

            if (field.isBarrier(pt)) {
                direction = Direction.random(dice);
            }
        } while (field.isBarrier(pt) && c++ < MAX);

        moving = true;

        super.move();
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Tree tree = filterOne(alsoAtPoint, Tree.class);
        if (tree != null) {
            return Elements.TREE;
        }
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
