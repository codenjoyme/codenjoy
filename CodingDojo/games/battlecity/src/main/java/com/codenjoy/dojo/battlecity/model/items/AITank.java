package com.codenjoy.dojo.battlecity.model.items;

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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class AITank extends Tank {

    public static final int MAX = 10;
    public static final int SHOOT_EVERY_TICKS = 10;
    public boolean dontShoot = false;
    private int act;

    public AITank(Point pt, Direction direction, Dice dice) {
        super(pt, direction, dice, 1);
    }

    @Override
    public void move() {
        shootIfReady();

        int c = 0;
        Point pt;
        do {
            pt = direction.change(this);

            // !field.isRiver(pt) потому что мы хотим сделать так, чтобы боты пытались
            // пройти через речку но не могли - это даст иллюзию, что
            // они пытаются отстрливаться через воду
            if (field.isBarrier(pt) && !field.isRiver(pt)) {
                direction = Direction.random(dice);
            }
        } while (field.isBarrier(pt) && c++ < MAX);

        moving = true;

        super.move();
    }

    private void shootIfReady() {
        if (dontShoot) {
            return;
        }

        if (act++ % SHOOT_EVERY_TICKS == 0) {
            act();
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Tree tree = filterOne(alsoAtPoint, Tree.class);
        if (tree != null) {
            return Elements.TREE;
        }

        if (!isAlive()) {
            return Elements.BANG;
        }

        Elements sub = subState();
        if (sub != null) {
            return sub;
        }

        switch (direction) {
            case LEFT:  return Elements.AI_TANK_LEFT;
            case RIGHT: return Elements.AI_TANK_RIGHT;
            case UP:    return Elements.AI_TANK_UP;
            case DOWN:  return Elements.AI_TANK_DOWN;
            default: throw new RuntimeException("Неправильное состояние танка!");
        }
    }

    protected Elements subState() {
        return null;
    }

}
