package com.codenjoy.dojo.services.algs;

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
        Point goal = goals.get(0); // TODO добавить все цели
        boolean[] goes = ways.get(from).goes();
        Status status = points.add(from);
        for (int index = 0; index < goes.length; index++) {
            if (!goes[index]) continue;

            Direction direction = Direction.valueOf(index);
            status.add(direction);
            queue.add(new Vector(from, direction, goal, pathLength));
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
