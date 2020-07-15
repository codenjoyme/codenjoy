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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

public class AITankPrize extends AITank {

    public static final int CHANGE_AFTER_TICKS = 4;
    private List<Bullet> hitsTarget;
    private int hitKills;
    private int ticksCount = 0;

    public AITankPrize(Point pt, Dice dice, Direction direction, int hitKills) {
        super(pt, dice, direction);
        this.hitKills = hitKills;
        this.hitsTarget = new LinkedList<Bullet>();
    }

    public void kill(Bullet bullet) {
        hitsTarget.add(bullet);

        if (hitsTarget.size() == hitKills) {
            super.kill(bullet);
            hitsTarget.clear();
        } else {
            //do nothing
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (isAlive()) {
            if (ticksCount <= CHANGE_AFTER_TICKS) {
                this.ticksCount++;
                switch (direction) {
                    case LEFT:  return Elements.AI_TANK_LEFT;
                    case RIGHT: return Elements.AI_TANK_RIGHT;
                    case UP:    return Elements.AI_TANK_UP;
                    case DOWN:  return Elements.AI_TANK_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            } else {
                return Elements.AI_TANK_PRIZE;
            }
        } else {
            return Elements.BANG;
        }
    }

    protected boolean isTankPrize() {
        return true;
    }
}
