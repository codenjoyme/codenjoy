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

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    public Collection<Point> getBarriers() {
        Collection<Point> all = getWalls();
        // add other barriers here
        return removeDuplicates(all);
    }

    public Collection<Point> getWalls() {
        return get(walls().toArray(new Elements[0]));
    }

    public boolean isBarrierAt(Point pt) {
        return getBarriers().contains(pt);
    }

    public Point getMe() {
        List<Point> list = get(heroes().toArray(new Elements[0]));

        return (list.isEmpty()) ? null : list.get(0);
    }

    public boolean isGameOver() {
        return !get(HERO_DIE).isEmpty();
    }

    public boolean isEnemyAt(Point pt) {
        return is(pt, enemies());
    }

    public boolean is(Point pt, List<Elements> enemies) {
        return getAllAt(pt).stream()
                .anyMatch(el -> enemies.contains(el));
    }

    public boolean isOtherHeroAt(Point pt) {
        return is(pt, otherHeroes());
    }

    public boolean isWall(Point pt) {
        return is(pt, walls());
    }

    public boolean isGold(Point pt) {
        return is(pt, gold());
    }

    public boolean isLadder(Point pt) {
        return is(pt, ladders());
    }

    public boolean isPipe(Point pt) {
        return is(pt, pipes());
    }
}