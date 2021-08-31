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


import com.codenjoy.dojo.battlecity.model.*;
import com.codenjoy.dojo.games.battlecity.Element;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.MovingObject;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;

import java.util.function.Consumer;

import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class Bullet extends MovingObject implements State<Element, Player> {

    private final Field field;
    private Tank owner;
    private Consumer<Object> onDestroy;
    private boolean heavy;
    private int tick;

    public Bullet(Field field, Direction tankDirection,
                  Point from, Tank owner,
                  Consumer<Object> onDestroy)
    {
        super(from.getX(), from.getY(), tankDirection);
        this.field = field;
        this.owner = owner;
        moving = true;
        this.onDestroy = onDestroy;
        speed = 2;
        heavy = false;
    }

    public int getTick() {
        return tick;
    }

    public void remove() {
        moving = false;
        if (onDestroy != null) {
            onDestroy.accept(this);
        }
    }

    @Override
    public void moving(Point pt) {
        tick++;
        if (pt.isOutOf(field.size())) {
            remove(); // TODO заимплементить взрыв
        } else {
            move(pt);
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
    public Element state(Player player, Object... alsoAtPoint) {
        Tree tree = filterOne(alsoAtPoint, Tree.class);
        if (tree != null) {
            return Element.TREE;
        }

        if (destroyed()) {
            return Element.BANG;
        } else {
            return Element.BULLET;
        }
    }

    public void heavy() {
        heavy = true;
    }

    public boolean isHeavy() {
        return heavy;
    }
}
