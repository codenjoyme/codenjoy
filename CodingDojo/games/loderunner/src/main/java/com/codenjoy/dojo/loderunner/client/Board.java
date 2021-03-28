package com.codenjoy.dojo.loderunner.client;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.loderunner.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.List;

import static com.codenjoy.dojo.loderunner.model.Elements.*;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public Collection<Point> getBarriers() {
        Collection<Point> all = getWalls();
        return removeDuplicates(all);
    }

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    public Collection<Point> getWalls() {
        return get(BRICK,
                UNDESTROYABLE_WALL);
    }

    public boolean isBarrierAt(Point pt) {
        return getBarriers().contains(pt);
    }

    public Point getMe() {
        List<Point> list = get(HERO_DIE,
                HERO_DRILL_LEFT,
                HERO_DRILL_RIGHT,
                HERO_LADDER,
                HERO_LEFT,
                HERO_RIGHT,
                HERO_FALL_LEFT,
                HERO_FALL_RIGHT,
                HERO_PIPE_LEFT,
                HERO_PIPE_RIGHT,

                HERO_SHADOW_DIE,
                HERO_SHADOW_DRILL_LEFT,
                HERO_SHADOW_DRILL_RIGHT,
                HERO_SHADOW_LADDER,
                HERO_SHADOW_LEFT,
                HERO_SHADOW_RIGHT,
                HERO_SHADOW_FALL_LEFT,
                HERO_SHADOW_FALL_RIGHT,
                HERO_SHADOW_PIPE_LEFT,
                HERO_SHADOW_PIPE_RIGHT);

        return (list.isEmpty()) ? null : list.get(0);
    }

    public boolean isGameOver() {
        return !get(HERO_DIE).isEmpty();
    }

    public List<Point> enemy() {
        return get(ENEMY_LADDER,
                ENEMY_LEFT,
                ENEMY_PIPE_LEFT,
                ENEMY_PIPE_RIGHT,
                ENEMY_RIGHT,
                ENEMY_PIT);
    }

    public List<Point> otherHero() {
        return get(OTHER_HERO_DIE,
                OTHER_HERO_DRILL_LEFT,
                OTHER_HERO_DRILL_RIGHT,
                OTHER_HERO_LADDER,
                OTHER_HERO_LEFT,
                OTHER_HERO_RIGHT,
                OTHER_HERO_FALL_LEFT,
                OTHER_HERO_FALL_RIGHT,
                OTHER_HERO_PIPE_LEFT,
                OTHER_HERO_PIPE_RIGHT,

                OTHER_HERO_SHADOW_DIE,
                OTHER_HERO_SHADOW_DRILL_LEFT,
                OTHER_HERO_SHADOW_DRILL_RIGHT,
                OTHER_HERO_SHADOW_LEFT,
                OTHER_HERO_SHADOW_RIGHT,
                OTHER_HERO_SHADOW_LADDER,
                OTHER_HERO_SHADOW_FALL_LEFT,
                OTHER_HERO_SHADOW_FALL_RIGHT,
                OTHER_HERO_SHADOW_PIPE_LEFT,
                OTHER_HERO_SHADOW_PIPE_RIGHT);
    }

    public List<Point> wall() {
        return get(BRICK,
                UNDESTROYABLE_WALL);
    }

    public List<Point> gold() {
        return get(YELLOW_GOLD,
                GREEN_GOLD,
                RED_GOLD);
    }

    public List<Point> ladder() {
        return get(LADDER,
                HERO_LADDER,
                HERO_SHADOW_LADDER,
                OTHER_HERO_LADDER,
                OTHER_HERO_SHADOW_LADDER,
                ENEMY_LADDER);
    }

    public List<Point> pipe() {
        return get(PIPE,
                HERO_PIPE_LEFT,
                HERO_PIPE_RIGHT,
                HERO_SHADOW_PIPE_LEFT,
                HERO_SHADOW_PIPE_RIGHT,
                OTHER_HERO_PIPE_LEFT,
                OTHER_HERO_PIPE_RIGHT,
                OTHER_HERO_SHADOW_PIPE_LEFT,
                OTHER_HERO_SHADOW_PIPE_RIGHT);
    }
}