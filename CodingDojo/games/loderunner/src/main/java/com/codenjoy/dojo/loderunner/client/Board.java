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
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

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
        return get(Elements.BRICK, Elements.UNDESTROYABLE_WALL);
    }

    public boolean isBarrierAt(int x, int y) {
        return getBarriers().contains(pt(x, y));
    }

    public Point getMe() {
        return get(Elements.HERO_DIE,
                Elements.HERO_DRILL_LEFT,
                Elements.HERO_DRILL_RIGHT,
                Elements.HERO_FALL_RIGHT,
                Elements.HERO_FALL_LEFT,
                Elements.HERO_LADDER,
                Elements.HERO_LEFT,
                Elements.HERO_RIGHT,
                Elements.HERO_PIPE_LEFT,
                Elements.HERO_PIPE_RIGHT).get(0);
    }

    public boolean isGameOver() {
        return !get(Elements.HERO_DIE).isEmpty();
    }

    public boolean isEnemyAt(int x, int y) {
        return isAt(x, y, Elements.ENEMY_LADDER) ||
                isAt(x, y, Elements.ENEMY_LEFT) ||
                isAt(x, y, Elements.ENEMY_PIPE_LEFT) ||
                isAt(x, y, Elements.ENEMY_PIPE_RIGHT) ||
                isAt(x, y, Elements.ENEMY_RIGHT) ||
                isAt(x, y, Elements.ENEMY_PIT);
    }

    public boolean isOtherHeroAt(int x, int y) {
        return isAt(x, y, Elements.OTHER_HERO_LEFT) ||
                isAt(x, y, Elements.OTHER_HERO_RIGHT) ||
                isAt(x, y, Elements.OTHER_HERO_LADDER) ||
                isAt(x, y, Elements.OTHER_HERO_PIPE_LEFT) ||
                isAt(x, y, Elements.OTHER_HERO_PIPE_RIGHT);
    }

    public boolean aWall(int x, int y) {
        return isAt(x, y, Elements.BRICK) ||
                isAt(x, y, Elements.UNDESTROYABLE_WALL);
    }

    public boolean aGold(int x, int y) {
        return isAt(x, y, Elements.GOLD);
    }

    public boolean aLadder(int x, int y) {
        return isAt(x, y, Elements.LADDER) ||
                isAt(x, y, Elements.HERO_LADDER) ||
                isAt(x, y, Elements.ENEMY_LADDER);
    }

    public boolean aPipe(int x, int y) {
        return isAt(x, y, Elements.PIPE) ||
                isAt(x, y, Elements.HERO_PIPE_LEFT) ||
                isAt(x, y, Elements.HERO_PIPE_RIGHT) ||
                isAt(x, y, Elements.OTHER_HERO_PIPE_LEFT) ||
                isAt(x, y, Elements.OTHER_HERO_PIPE_RIGHT);
    }
}
