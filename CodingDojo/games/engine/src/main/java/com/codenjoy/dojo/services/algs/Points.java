package com.codenjoy.dojo.services.algs;

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
