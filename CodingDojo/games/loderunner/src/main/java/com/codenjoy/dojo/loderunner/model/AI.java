package com.codenjoy.dojo.loderunner.model;

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
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.*;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

// TODO почему-то эта реализация быстрее чем базовая DeikstraFindWay узнать почему и применить эту идею там, а тут заюзать повторно
public class AI implements EnemyAI {

    Map<Point, List<Direction>> possibleWays = new TreeMap<>();

    @Override
    public Direction getDirection(Field field, Point from, Point to) {
        if (to == null) return null;

        setupPossibleWays(field);

        Direction direction = null;
        List<Direction> path = getPath(field.size(), from, to);
        if (!path.isEmpty()) {
            direction = path.get(0);
        }

        return direction;
    }

    List<Direction> getPath(int size, Point from, Point to) {
        return getPath(size, from).get(to);
    }

    private Map<Point, List<Direction>> getPath(int size, Point from) {
        Map<Point, List<Direction>> path = new HashMap<>();
        for (Point point : possibleWays.keySet()) {
            path.put(point, new LinkedList<>());
        }

        boolean[][] processed = new boolean[size][size];
        LinkedList<Point> toProcess = new LinkedList<>();

        Point current = from;
        do {
            if (current == null) {
                current = toProcess.remove();
            }
            List<Direction> before = path.get(current);
            for (Direction direction : possibleWays.get(current)) {
                Point to = direction.change(current);
//                if (field.isEnemyAt(to.getX(), to.getY())) continue;
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

    void setupPossibleWays(Field field) {
        if (possibleWays.isEmpty()) {
            for (int x = 0; x < field.size(); x++) {
                for (int y = 0; y < field.size(); y++) {
                    Point pt = pt(x, y);
                    List<Direction> directions = new LinkedList<>();
                    for (Direction direction : Arrays.asList(UP, DOWN, LEFT, RIGHT)) {
                        if (isPossible(field, pt, direction)) {
                            directions.add(direction);
                        }
                    }
                    possibleWays.put(pt, directions);
                }
            }
        }
    }

    private boolean isPossible(Field field, Point pt, Direction direction) {
        if (field.isBrick(pt) || field.isBorder(pt)) return false;

        Point newPt = direction.change(pt);
        int x = newPt.getX();
        int y = newPt.getY();

        if (isOutOfField(field.size(), x, y)) return false;

        if (field.isBrick(newPt) || field.isBorder(newPt)) return false;

        if (direction == Direction.UP && !field.isLadder(pt)) return false;

        Point under = DOWN.change(pt);
        if (!isOutOfField(field.size(), pt.getX(), pt.getY() - 1) &&
                !field.isBrick(under) &&
                !field.isLadder(under) &&
                !field.isBorder(under) &&
                !field.isLadder(pt) &&
                !field.isPipe(pt) &&
                direction != DOWN) return false;

        return true;
    }

    private boolean isOutOfField(int size, int x, int y) {
        return x < 0 || y < 0 || x > size - 1 || y > size - 1;
    }
}
