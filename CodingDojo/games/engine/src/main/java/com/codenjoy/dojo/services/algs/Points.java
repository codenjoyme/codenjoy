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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Оптимизированная версия Map<Point, Status>.
 * Служит одной цели - понять в этой клеточке расмотрели ли мы все возможные direction
 * куда можно еще двигаться или нет.
 */
public class Points {

    private Status[][] all;

    public Points(int size) {
        all = new Status[size][size];;
    }

    private Status set(Point pt, Status status) {
        return all[pt.getX()][pt.getY()] = status;
    }

    public Status get(Point pt) {
        return all[pt.getX()][pt.getY()];
    }

    public Status add(Point pt) {
        return set(pt, new Status());
    }

    public boolean done(Vector next) {
        return get(next.from()).done(next.where());
    }

    public boolean isDone(Point pt) {
        Status status = get(pt);
        if (status == null) {
            return false;
        }

        return status.empty();
    }

    public boolean isAdded(Point pt) {
        return get(pt) != null;
    }

    // not optimized
    public Map<Point, List<Direction>> toMap() {
        Map<Point, List<Direction>> map = new HashMap<>();
        for (int x = 0; x < all.length; x++) {
            for (int y = 0; y < all[0].length; y++) {
                Point pt = pt(x, y);
                Status status = get(pt);
                map.put(pt, status.directions());
            }
        }
        return map;
    }
}
