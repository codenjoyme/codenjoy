package com.codenjoy.dojo.spacerace.model;

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

/**
 * Артефакт пуля на поле
 */
public class Bullet extends PointImpl implements Tickable, State<Elements, Player> {

    private Direction direction;
    private Hero hero;

    public Bullet(Point pt, Hero hero) {
        super(pt);
        this.hero = hero;
    }

    public Bullet(int x, int y, Hero hero) {
        super(x, y);
        direction = Direction.UP;
        this.hero = hero;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BULLET;
    }

    @Override
    public void tick() {
        int newX = direction.changeX(x);
        int newY = direction.changeY(y);
        move(newX, newY);
    }

    public Hero getOwner() {
        return hero;
    }
}
