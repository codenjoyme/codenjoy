package com.codenjoy.dojo.services.algs;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

public class SnakeFindWay {

    private Point from;
    private Point to;
    private Direction go;

    public SnakeFindWay(Point from, Point to, Direction currentDirection) {
        this.from = from;
        this.to = to;
        this.go = currentDirection;
    }

    public boolean isBarrierInFront(Point barrier, Direction direction) {
        return direction.change(from).equals(barrier);
    }

    private Direction getPossibleDirection(List<Point> barriers, Direction currentDirection) {
        if (barriers.isEmpty()) {
            return currentDirection;
        }

        Set<Direction> directions = getAll();

        for (Point barrier : barriers) {
            for (Direction direction : directions) {
                if (isBarrierInFront(barrier, direction)) {
                    directions.remove(direction);
                    break;
                }
            }
        }

        return selectClockwiseFrom(directions, currentDirection);
    }

    private Direction selectClockwiseFrom(Set<Direction> directions, Direction currentDirection) {
        if (directions.size() == 0) { // а вот тут TODO
            throw new RuntimeException("No variants");
        }
        if (directions.size() == 1) {  // единственный доступный путь
            return directions.iterator().next();
        }
        if (directions.contains(currentDirection)) {  // зачем куда-то вращаться, если прямой путь свободен
            return currentDirection;
        }

        boolean contains;
        Direction direction = currentDirection;
        do {
            direction = direction.clockwise();
            contains = directions.contains(direction);
        } while (!contains);
        return direction;
    }

    public Direction get(List<Point> barriers) {
        return getPossibleDirection(barriers, go());
    }

    private Direction go() {
        if (go.equals(Direction.LEFT)) {
            if (sameY()) {
                if (left()) {
                    return go;
                }
            }

            if (sameX() || right()) {
                if (down()) {
                    return Direction.DOWN;
                } else {
                    return Direction.UP;
                }
            }
        } else if (go.equals(Direction.RIGHT)) {
            if (sameY()) {
                if (right()) {
                    return go;
                }
            }

            if (sameX() || left()) {
                if (down()) {
                    return Direction.DOWN;
                } else {
                    return Direction.UP;
                }
            }
        } else if (go.equals(Direction.DOWN)) {
            if (sameX()) {
                if (down()) {
                    return go;
                }
            }

            if (sameY() || up()) {
                if (left()) {
                    return Direction.LEFT;
                } else {
                    return Direction.RIGHT;
                }
            }
        } else if (go.equals(Direction.UP)) {
            if (sameX()) {
                if (up()) {
                    return go;
                }
            }

            if (sameY() || down()) {
                if (left()) {
                    return Direction.LEFT;
                } else {
                    return Direction.RIGHT;
                }
            }
        }
        return go;
    }

    private boolean up() {
        return to.getY() < from.getY();
    }

    private boolean down() {
        return to.getY() > from.getY();
    }

    private boolean sameY() {
        return to.getY() == from.getY();
    }

    private boolean left() {
        return to.getX() < from.getX();
    }

    private boolean right() {
        return to.getX() > from.getX();
    }

    private boolean sameX() {
        return to.getX() == from.getX();
    }

    public Set<Direction> getAll() {
        Set<Direction> result = new HashSet<Direction>();
        result.addAll(Arrays.asList(Direction.LEFT, Direction.RIGHT, Direction.UP, Direction.DOWN));
        return result;
    }
}
