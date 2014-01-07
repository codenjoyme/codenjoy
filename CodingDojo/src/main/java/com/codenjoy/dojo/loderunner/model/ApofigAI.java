package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * User: sanja
 * Date: 07.01.14
 * Time: 20:09
 */
public class ApofigAI implements EnemyAI {

    Map<Point, List<Direction>> possibleWays = new TreeMap<Point, List<Direction>>();

    @Override
    public Direction getDirection(Field field, Point me) {
        setupPossibleWays(field);

        Point destination = field.getHeroes().get(0); // TODO че он за одним гнаться всегда будет?

        return getPath(me, destination).get(0);
    }

    List<Direction> getPath(Point from, Point to) {
        return getPath(from).get(to);
    }

    private Map<Point, List<Direction>> getPath(Point from) {
        Map<Point, List<Direction>> path = new HashMap<Point, List<Direction>>();
        for (Point point : possibleWays.keySet()) {
            path.put(point, new LinkedList<Direction>());
        }

        List<Point> processed = new LinkedList<Point>();

        Point current = from;
        while (current != null) {
            List<Direction> before = path.get(current);
            for (Direction direction : possibleWays.get(current)) {
                Point to = direction.change(current);
                if (processed.contains(to)) continue;

                List<Direction> directions = path.get(to);
                if (directions.isEmpty() || directions.size() > before.size() + 1) {
                    directions.addAll(before);
                    directions.add(direction);
                }
            }

            Point next = null;
            for (Direction direction : possibleWays.get(current)) {
                Point to = direction.change(current);
                if (processed.contains(to)) continue;

                if (next == null) {
                    next = to;
                    continue;
                }

                if (path.get(next).size() > path.get(to).size()) {
                    next = to;
                }
            }
            processed.add(current);
            current = next;
        }
        return path;
    }

    void setupPossibleWays(Field field) {
        if (possibleWays.isEmpty()) {
            for (int x = 0; x < field.size(); x++) {
                for (int y = 0; y < field.size(); y++) {
                    Point pt = pt(x, y);
                    List<Direction> directions = new LinkedList<Direction>();
                    for (Direction direction : Direction.values()) {
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
        if (field.isBrick(pt.getX(), pt.getY()) || field.isBorder(pt.getX(), pt.getY())) return false;

        Point newPt = direction.change(pt);
        int x = newPt.getX();
        int y = newPt.getY();

        if (isOutOfField(field.size(), x, y)) return false;

        if (field.isBrick(x, y) || field.isBorder(x, y)) return false;

        if (direction == Direction.UP && !field.isLadder(pt.getX(), pt.getY())) return false;

        if (!isOutOfField(field.size(), pt.getX(), pt.getY() - 1) &&
            !field.isBrick(pt.getX(), pt.getY() - 1) &&
            !field.isLadder(pt.getX(), pt.getY() - 1) &&
            !field.isBorder(pt.getX(), pt.getY() - 1) &&
                !field.isLadder(pt.getX(), pt.getY()) &&
                !field.isPipe(pt.getX(), pt.getY()) &&
                direction != Direction.DOWN) return false;

        return true;
    }

    private boolean isOutOfField(int size, int x, int y) {
        return x < 0 || y < 0 || x > size - 1 || y > size - 1;
    }
}
