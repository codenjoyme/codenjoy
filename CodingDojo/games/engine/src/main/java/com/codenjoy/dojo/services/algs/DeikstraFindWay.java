package com.codenjoy.dojo.services.algs;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static java.util.stream.Collectors.toList;

public class DeikstraFindWay {

    private static final List<Direction> DIRECTIONS = Arrays.asList(UP, DOWN, LEFT, RIGHT);

    // карта возможных передвижений, которые не будут менять на этом уровне: стены и прочие препятствия
    private Map<Point, List<Direction>> basic;

    // карта возможных передвижений дополненная движимыми объектами
    private Map<Point, List<Direction>> dynamic;

    private int size;
    private Possible checker;
    private boolean possibleIsConstant;

    public DeikstraFindWay() {
        this(false);
    }

    /**
     * @param possibleIsConstant Бывают случаи, когда варианты куда двигаться на карте
     * не меняется от тика к тику, а потому не надо пересчитывать варианты движений всякий раз.
     * Вот в таких случаях мы и ставим тут true.
     */
    public DeikstraFindWay(boolean possibleIsConstant) {
        this.possibleIsConstant = possibleIsConstant;
    }

    public interface Possible {

        default boolean possible(Point from, Direction where) {
            return true;
        }

        default boolean possible(Point point) {
            return true;
        }

        default boolean check(int size, Point from, Direction where) {
            if (from.isOutOf(size)) return false;
            if (!possible(from)) return false;

            Point dest = where.change(from);

            if (dest.isOutOf(size)) return false;
            if (!possible(dest)) return false;

            if (!possible(from, where)) return false;

            return true;
        }
    }

    public List<Direction> getShortestWay(int size, Point from, List<Point> goals, Possible possible) {
        if (possible == null) {
            throw new RuntimeException("Please setup Possible object before run getShortestWay");
        }
        getPossibleWays(size, possible);

        return buildPath(from, goals);
    }

    public List<Direction> buildPath(Point from, List<Point> goals) {
        List<List<Direction>> paths = new LinkedList<>();
        Map<Point, List<Direction>> pathMap = getPath(from, goals);
        for (Point to : goals) {
            List<Direction> path = pathMap.get(to);
            if (path == null || path.isEmpty()) continue;
            paths.add(path);
        }

        int minDistance = Integer.MAX_VALUE;
        int indexMin = 0;
        for (int index = 0; index < paths.size(); index++) {
            List<Direction> path = paths.get(index);
            if (minDistance > path.size()) {
                minDistance = path.size();
                indexMin = index;
            }
        }

        if (paths.isEmpty()) return Arrays.asList();
        List<Direction> shortest = paths.get(indexMin);
        if (shortest.size() == 0) return Arrays.asList();

        return shortest;
    }

    private static class Vector implements Comparable<Vector> {
        private Point to;
        private Point from;
        private Direction where;
        private double distance;

        public Vector(Point from, Direction where, Point goal, int pathLength) {
            this.from = from;
            this.where = where;
            this.to = where.change(from);
            this.distance = to.distance(goal) + pathLength;
        }

        @Override
        public String toString() {
            return String.format("Vector{%s->%s, %s, %s}",
                    from,
                    to,
                    where.name().charAt(0),
                    distance);
        }

        @Override
        public int compareTo(Vector o) {
            return Double.compare(distance, o.distance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vector vector = (Vector) o;
            return Objects.equals(from, vector.from) &&
                    where == vector.where;
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, where);
        }
    }

    private class Status {
        boolean[] goes = new boolean[4];

        public void add(Direction direction) {
            goes[direction.value()] = true;
        }

        public boolean done(Direction direction) {
            boolean result = goes[direction.value()];
            goes[direction.value()] = false;
            return result;
        }

        public boolean empty() {
            return goes[LEFT.value()]
                    && goes[RIGHT.value()]
                    && goes[UP.value()]
                    && goes[DOWN.value()];
        }
    }

    private class Points {
        private Status[][] all;

        public Points(int size) {
            all = new Status[size][size];;
        }

        private Status set(Point pt, Status status) {
            return all[pt.getX()][pt.getY()] = status;
        }

        private Status get(Point pt) {
            return all[pt.getX()][pt.getY()];
        }

        public Status add(Point pt) {
            return set(pt, new Status());
        }

        public boolean done(Vector next) {
            return get(next.from).done(next.where);
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
    }

    private class Vectors {
        private List<Vector> queue;
        private Points points;

        public Vectors(int size) {
            queue = new LinkedList<>();
            points = new Points(size);
        }

        public void add(List<Point> goals, Point from, int pathLength) {
            Point goal = goals.get(0); // TODO добавить все цели
            List<Direction> directions = ways().get(from);
            Status status = points.add(from);
            for (Direction direction : directions) {
                status.add(direction);
                queue.add(new Vector(from, direction, goal, pathLength));
            }

            Collections.sort(queue);
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public Vector next() {
            Vector next = null;
            while (!queue.isEmpty()) {
                next = queue.remove(0);
                if (points.done(next)) {
                    break;
                }
            }
            return next;
        }

        public boolean processed(Point from) {
            return points.isDone(from);
        }

        public boolean wasHere(Point from) {
            return points.isAdded(from);
        }
    }

    private Map<Point, List<Direction>> getPath(Point from, List<Point> inputGoals) {
        Set<Point> goals = new HashSet<>(inputGoals);
        Map<Point, List<Direction>> path = new HashMap<>();
        for (Point point : ways().keySet()) {
            path.put(point, new ArrayList<>(100));
        }

        Vectors vectors = new Vectors(size);

        vectors.add(inputGoals, from, 0);
        Vector current;
        while (!goals.isEmpty() && (current = vectors.next()) != null) {

            List<Direction> before = path.get(current.from);

            if (vectors.wasHere(current.to)) continue;

            List<Direction> directions = path.get(current.to);
            if (before.size() < directions.size() - 1) {
                // мы нашли более короткий путь,
                // но это никогда не случится )
                directions.clear();
            }
            if (directions.isEmpty()) {
                if (!before.isEmpty()) {
                    directions.addAll(before);
                }
                directions.add(current.where);

                if (!vectors.processed(current.to)) {
                    vectors.add(inputGoals, current.to, directions.size());
                }
            } else {
                // do nothing
            }
            goals.remove(current.from);
        }

        return path;
    }

    private Map<Point, List<Direction>> ways() {
        return (dynamic != null) ? dynamic : basic;
    }

    private Map<Point, List<Direction>> setupWays() {
        Map<Point, List<Direction>> result = new TreeMap<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point from = pt(x, y);
                List<Direction> directions = new LinkedList<>();
                for (Direction direction : DIRECTIONS) {
                    if (!checker.check(size, from, direction)) continue;

                    directions.add(direction);
                }
                result.put(from, directions);
            }
        }
        return result;
    }

    public void updateWays(Possible possible) {
        dynamic = basic.entrySet().stream()
                .map(entry -> update(possible, entry))
                .collect(toMap());
    }

    public Collector<Map.Entry<Point, List<Direction>>, ?, Map<Point, List<Direction>>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    private Map.Entry<Point, List<Direction>> update(Possible possible, Map.Entry<Point, List<Direction>> entry) {
        List<Direction> directions = entry.getValue();
        Point point = entry.getKey();

        List<Direction> updated = directions.stream()
                .filter(direction -> possible.check(size, point, direction))
                .collect(toList());

        return new AbstractMap.SimpleEntry(point, updated);
    }

    public Map<Point, List<Direction>> getPossibleWays(int size, Possible possible) {
        this.size = size;
        this.checker = possible;

        if (possibleIsConstant && basic != null) {
            return basic;
        }

        return basic = setupWays();
    }

    public Map<Point, List<Direction>> getBasic() {
        return basic;
    }

    public Map<Point, List<Direction>> getDynamic() {
        return dynamic;
    }
}
