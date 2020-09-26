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

import static com.codenjoy.dojo.bomberman.model.Elements.DEAD_MEAT_CHOPPER;
import static com.codenjoy.dojo.bomberman.model.Elements.MEAT_CHOPPER;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

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

        return MEAT_CHOPPER;
    }

    @Override
    public void tick() {
        // неугомонные чоперы в тестах только так останавливаются
        if (stop) {
            return;
        }

        Point from = this;
        if (direction == null
            || dice.next(5) == 0
            || field.walls().itsMe(direction.change(from)))
        {
            direction = selectNew(from);
        }

        if (direction != null) {
            move(direction.change(from));
        }
    }

    private Direction selectNew(Point from) {
        int iteration = 0;
        Point to;
        Direction direction;
        Set<Direction> all = new HashSet<>();
        do {
            int n = 4;
            int move = dice.next(n);
            direction = Direction.valueOf(move);
            all.add(direction);

            to = direction.change(from);
        } while (barrier(to) && iteration++ < MAX && all.size() < 4);

        if (iteration >= MAX) {
            return null;
        }

        return direction;
    }

    private boolean barrier(Point to) {
        return field.walls().itsMe(to) || to.isOutOf(field.size());
    }
}
