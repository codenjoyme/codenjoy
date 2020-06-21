package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.*;

import java.util.HashSet;
import java.util.Set;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.bomberman.model.StateUtils.filterOne;

public class MeatChopper extends Wall implements State<Elements, Player>, Tickable {

    public static final int MAX = 100;

    protected Dice dice;
    protected Field field;
    protected Direction direction;
    protected boolean stop = false;

    public MeatChopper(Point pt, Field field, Dice dice) {
        super(pt);
        this.field = field;
        this.dice = dice;
    }

    public MeatChopper(int x, int y) {
        super(x, y);
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public Wall copy() {
        return new MeatChopper(this.x, this.y);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        stop = false;
        this.direction = direction;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Blast blast = filterOne(alsoAtPoint, Blast.class);
        if (blast != null) {
            return DEAD_MEAT_CHOPPER;
        }

        DestroyWall wall = filterOne(alsoAtPoint, DestroyWall.class);
        if (wall != null) {
            return DESTROYED_WALL;
        }

        return MEAT_CHOPPER;
    }
}
