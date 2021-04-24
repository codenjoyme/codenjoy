package com.codenjoy.dojo.services.algs;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * Дает ответ на вопрос куда еще можно сходить, при этом выбирая
 * наиболее оптимальную по мнениб алгоритма поиска кратчайшего пути A* координату
 * и направление движения от нее.
 */
public class Vectors {

    private List<Vector> queue;
    private Points points;
    private Points ways;

    public Vectors(int size, Points ways) {
        this.ways = ways;
        queue = new SortedVectors();
        points = new Points(size);
    }

    public void add(List<Point> goals, Point from, int pathLength) {
        boolean[] goes = ways.get(from).goes();
        Status status = points.add(from);
        for (int index = 0; index < goes.length; index++) {
            if (!goes[index]) continue;

            Direction direction = Direction.valueOf(index);
            status.add(direction);
            queue.add(new Vector(from, direction, goals, pathLength));
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Vector next() {
        Vector next = null;
        while (!queue.isEmpty()) {
            next = get();
            if (points.done(next)) {
                break;
            }
        }
        return next;
    }

    public Vector get() {
        return queue.remove(0);
    }

    public boolean processed(Point from) {
        return points.isDone(from);
    }

    public boolean wasHere(Point from) {
        return points.isAdded(from);
    }
}
