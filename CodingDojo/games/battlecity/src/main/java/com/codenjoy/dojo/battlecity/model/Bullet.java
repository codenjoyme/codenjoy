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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.MovingObject;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

public class Bullet extends MovingObject implements State<Elements, Player> {

    private Field field;
    private Tank owner;
    private OnDestroy onDestroy;

    public Bullet(Field field, Direction tankDirection, Point from, Tank owner, OnDestroy onDestroy) {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        this.owner = owner;
        moving = true;
        this.onDestroy = onDestroy;
        this.speed = 2;
    }

    public void onDestroy() {
        moving = false;
        if (onDestroy != null) {
            onDestroy.destroy(this);
        }
    }

    @Override
    public void moving(int newX, int newY) {
        if (field.outOfField(newX, newY)) {
            onDestroy(); // TODO заимплементить взрыв
        } else {
            x = newX;
            y = newY;
            field.affect(this);
        }
    }

    public Tank getOwner() {
        return owner;
    }

    public void boom() {
        moving = false;
        owner = null;
    }

    public boolean destroyed() {
        return owner == null;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (destroyed()) {
            return Elements.BANG;
        } else {
            return Elements.BULLET;
        }
    }
}
