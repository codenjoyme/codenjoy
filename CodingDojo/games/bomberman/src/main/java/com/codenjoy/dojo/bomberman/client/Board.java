package com.codenjoy.dojo.bomberman.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.bomberman.model.Elements;
import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public Point getBomberman() {
        return get(BOMBERMAN, BOMB_BOMBERMAN, DEAD_BOMBERMAN).get(0);
    }

    public Collection<Point> getOtherBombermans() {
        return get(OTHER_BOMBERMAN, OTHER_BOMB_BOMBERMAN, OTHER_DEAD_BOMBERMAN);
    }

    public boolean isGameOver() {
        return !get(DEAD_BOMBERMAN).isEmpty();
    }

    public Elements getAt(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            return WALL;
        }
        return super.getAt(x, y);
    }

    public Collection<Point> getBarriers() {
        Collection<Point> all = getMeatChoppers();
        all.addAll(getWalls());
        all.addAll(getBombs());
        all.addAll(getDestroyWalls());
        all.addAll(getOtherBombermans());

        return removeDuplicates(all);
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s\n" +
            "Bomberman at: %s\n" +
            "Other bombermans at: %s\n" +
            "Meat choppers at: %s\n" +
            "Destroy walls at: %s\n" +
            "Bombs at: %s\n" +
            "Blasts: %s\n" +
            "Expected blasts at: %s",
                boardAsString(),
                getBomberman(),
                getOtherBombermans(),
                getMeatChoppers(),
                getDestroyWalls(),
                getBombs(),
                getBlasts(),
                getFutureBlasts());
    }

    public Collection<Point> getMeatChoppers() {
        return get(MEAT_CHOPPER);
    }

    public Collection<Point> getWalls() {
        return get(WALL);
    }

    public Collection<Point> getDestroyWalls() {
        return get(DESTROY_WALL);
    }

    public Collection<Point> getBombs() {
        List<Point> result = new LinkedList<Point>();
        result.addAll(get(BOMB_TIMER_1));
        result.addAll(get(BOMB_TIMER_2));
        result.addAll(get(BOMB_TIMER_3));
        result.addAll(get(BOMB_TIMER_4));
        result.addAll(get(BOMB_TIMER_5));
        result.addAll(get(BOMB_BOMBERMAN));
        return result;
    }

    public Collection<Point> getBlasts() {
        return get(BOOM);
    }

    public Collection<Point> getFutureBlasts() {
        Collection<Point> result = new LinkedList<Point>();
        Collection<Point> bombs = getBombs();
        bombs.addAll(get(OTHER_BOMB_BOMBERMAN));
        bombs.addAll(get(BOMB_BOMBERMAN));

        for (Point bomb : bombs) {
            result.add(bomb);
            result.add(new PointImpl(bomb.getX() - 1, bomb.getY()));
            result.add(new PointImpl(bomb.getX() + 1, bomb.getY()));
            result.add(new PointImpl(bomb.getX()    , bomb.getY() - 1));
            result.add(new PointImpl(bomb.getX()    , bomb.getY() + 1));
        }
        for (Point blast : result.toArray(new Point[0])) {
            if (blast.isOutOf(size) || getWalls().contains(blast)) {
                result.remove(blast);
            }
        }
        return removeDuplicates(result);
    }

    public boolean isBarrierAt(int x, int y) {
        return getBarriers().contains(pt(x, y));
    }

}