package com.codenjoy.dojo.battlecity.model.items;

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

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

public class AITankPrize extends AITank {

    public static final int CHANGE_EVERY_TICKS = 4;
    private int damage;
    private int vitality;
    private int ticks;

    public AITankPrize(Point pt, Direction direction, int vitality, Dice dice) {
        super(pt, direction, dice);
        this.vitality = vitality;
        damage = 0;
        ticks = 0;
    }

    @Override
    public void tick() {
        super.tick();

        ticks++;
    }

    public void kill(Bullet bullet) {
        damage++;

        if (damage == vitality) {
            damage = 0;
            super.kill(bullet);
        }
    }

    @Override
    public Elements subState() {
        if (ticks % CHANGE_EVERY_TICKS == 0) {
            return Elements.AI_TANK_PRIZE;
        }
        return null;
    }

    protected boolean isTankPrize() {
        return true;
    }
}
