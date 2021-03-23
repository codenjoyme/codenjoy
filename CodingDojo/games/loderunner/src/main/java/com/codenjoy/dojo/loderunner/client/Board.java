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
        return get(HERO_DIE,
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
                HERO_SHADOW_PIPE_RIGHT).get(0);
    }

    public boolean isGameOver() {
        return !get(HERO_DIE).isEmpty();
    }

    public boolean isEnemyAt(Point pt) {
        return isAt(pt,
                ENEMY_LADDER,
                ENEMY_LEFT,
                ENEMY_PIPE_LEFT,
                ENEMY_PIPE_RIGHT,
                ENEMY_RIGHT,
                ENEMY_PIT);
    }

    public boolean isOtherHeroAt(Point pt) {
        return isAt(pt,
                OTHER_HERO_DIE,
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

    public boolean isWall(Point pt) {
        return isAt(pt,
                BRICK,
                UNDESTROYABLE_WALL);
    }

    public boolean isGold(Point pt) {
        return isAt(pt,
                YELLOW_GOLD,
                GREEN_GOLD,
                RED_GOLD);
    }

    public boolean isLadder(Point pt) {
        return isAt(pt,
                LADDER,
                HERO_LADDER,
                HERO_SHADOW_LADDER,
                OTHER_HERO_LADDER,
                OTHER_HERO_SHADOW_LADDER,
                ENEMY_LADDER);
    }

    public boolean isPipe(Point pt) {
        return isAt(pt,
                PIPE,
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
