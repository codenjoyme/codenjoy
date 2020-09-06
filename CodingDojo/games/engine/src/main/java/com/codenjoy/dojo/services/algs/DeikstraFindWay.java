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

import static com.codenjoy.dojo.services.PointImpl.pt;

public class DeikstraFindWay {

    private static final List<Direction> DIRECTIONS = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
    private Map<Point, List<Direction>> ways;
    private int size;
    private Possible checker;

    public interface Possible {

        default boolean possible(Point from, Direction direction) {
            return true;
        }

        default boolean possible(Point point) {
            return true;
        }

        default boolean check(int size, Point from, Direction direction) {
            if (!possible(from)) return false;

            Point to = direction.change(from);
            if (to.isOutOf(size)) return false;

            if (!possible(to)) return false;

            if (!possible(from, direction)) return false;

            return true;
        }
    }

    public List<Direction> getShortestWay(int size, Point from, List<Point> goals, Possible possible) {
        this.size = size;
        this.checker = possible;
        if (possible == null) {
            throw new RuntimeException("Please setup Possible object before run getShortestWay");
        }
        setupWays();

        List<List<Direction>> paths = new LinkedList<>();
        for (Point to : goals) {
            List<Direction> path = getPath(from).get(to);
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

    private Map<Point, List<Direction>> getPath(Point from) {
        Map<Point, List<Direction>> path = new HashMap<>();
        for (Point point : ways.keySet()) {
            path.put(point, new LinkedList<>());
        }

        boolean[][] processed = new boolean[size][size];
        LinkedList<Point> toProcess = new LinkedList<>();

        Point current = from;
        do {
            if (current == null) {
                if (toProcess.isEmpty()) { // TODO test me
                    break;
                }
                current = toProcess.remove();
            }
            List<Direction> before = path.get(current);
            for (Direction direction : ways.get(current)) {
                Point to = direction.change(current);
                if (!checker.possible(to)) continue;
                if (processed[to.getX()][to.getY()]) continue;

                List<Direction> directions = path.get(to);
                if (directions.isEmpty() || directions.size() > before.size() + 1) {
                    directions.addAll(before);
                    directions.add(direction);

                    if (!processed[to.getX()][to.getY()]) {
                        toProcess.add(to);
                    }
                }
            }
            processed[current.getX()][current.getY()] = true;
            current = null;
        } while (!toProcess.isEmpty());

        return path;
    }

    private void setupWays() {
        ways = new TreeMap<>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point from = pt(x, y);
                List<Direction> directions = new LinkedList<>();
                for (Direction direction : DIRECTIONS) {
                    if (checker.check(size, from, direction)) {
                        directions.add(direction);
                    }
                }
                ways.put(from, directions);
            }
        }
    }

    public Map<Point, List<Direction>> getPossibleWays() {
        return ways;
    }
}
