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

import static com.codenjoy.dojo.battlecity.services.GameSettings.Keys.*;

public class AITankPrize extends AITank {

    private int damage;
    private int ticks;
    private boolean wounded;

    public AITankPrize(Point pt, Direction direction, Dice dice) {
        super(pt, direction, dice);
        damage = 0;
        ticks = 0;
        wounded = false;
    }

    @Override
    public void tick() {
        super.tick();

        ticks++;
        wounded = false;
    }

    public void kill(Bullet bullet) {
        damage++;
        wounded = true;

        if (damage == vitality()) {
            damage = 0;
            super.kill(bullet);
        }
    }

    private int vitality() {
        return settings().integer(KILL_HITS_AI_PRIZE);
    }

    private int changeEveryTicks() {
        return settings().integer(AI_PRIZE_SPRITE_CHANGE_TICKS);
    }

    @Override
    public Elements subState() {
        if (ticks % changeEveryTicks() == 0 && !wounded) {
            return Elements.AI_TANK_PRIZE;
        }

        if (wounded) {
            return Elements.BANG;
        }
        return null;
    }

    protected boolean withPrize() {
        return true;
    }
}
