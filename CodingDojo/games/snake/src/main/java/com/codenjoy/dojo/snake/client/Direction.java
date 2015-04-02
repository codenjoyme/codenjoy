package com.codenjoy.dojo.snake.client;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:06 AM
 */
public class Direction {

    public static final String UP = "UP";
    public static final String DOWN = "DOWN";
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";

    private Point from;
    private Point to;
    private String currentDirection;

    public Direction(Point from, Point to, String currentDirection) {
        this.from = from;
        this.to = to;
        this.currentDirection = currentDirection;
    }

    public boolean isBarrierInFront(Point barrier, String direction) {
        if (LEFT.equals(direction)) {
            return (barrier.x == from.x - 1) && (barrier.y == from.y);
        } else if (RIGHT.equals(direction)) {
            return (barrier.x == from.x + 1) && (barrier.y == from.y);
        } else if (DOWN.equals(direction)) {
            return (barrier.x == from.x) && (barrier.y == from.y - 1);
        } else {
            return (barrier.x == from.x) && (barrier.y == from.y + 1);
        }
    }

    public static String inverted(String direction) {
        if (direction.equals(Direction.LEFT)) {
            return Direction.RIGHT;
        } else if (direction.equals(Direction.RIGHT)) {
            return Direction.LEFT;
        } else if (direction.equals(Direction.DOWN)) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    private String getPossibleDirection(List<Point> barriers, String currentDirection) {
        if (barriers.isEmpty()) {
            return currentDirection;
        }

        Set<String> directions = getAll();

        for (Point barrier : barriers) {
            for (String direction : directions) {
                if (isBarrierInFront(barrier, direction)) {
                    directions.remove(direction);
                    break;
                }
            }
        }

        return selectClockwiseFrom(directions, currentDirection);
    }

    private String selectClockwiseFrom(Set<String> directions, String currentDirection) {
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
        String direction = currentDirection;
        do {
            direction = nextClockwise(direction);
            contains = directions.contains(direction);
        } while (!contains);
        return direction;
    }

    private String nextClockwise(String direction) {
        if (direction.equals(LEFT)) {
            return UP;
        } else if (direction.equals(UP)) {
            return RIGHT;
        } else if (direction.equals(RIGHT)) {
            return DOWN;
        } else {
            return LEFT;
        }
    }

    public String get(List<Point> barriers) {
        return getPossibleDirection(barriers, getDirectionIgnoredBarriers());
    }

    private String getDirectionIgnoredBarriers() {
        if (currentDirection.equals(LEFT)) {
            if (from.y == to.y) {
                if (from.x > to.x) {
                    return currentDirection;
                }
            }

            if (from.x == to.x) {
                if (from.y < to.y) {
                    return UP;
                }
                if (from.y > to.y) {
                    return DOWN;
                }
            }

            if (from.x < to.x) {
                if (from.y > to.y) {
                    return DOWN;
                } else if (from.y < to.y) {
                    return UP;
                } else {
                    return UP;
                }
            }
        } else if (currentDirection.equals(RIGHT)) {
            if (from.y == to.y) {
                if (from.x < to.x) {
                    return currentDirection;
                }
            }

            if (from.x == to.x) {
                if (from.y < to.y) {
                    return UP;
                }
                if (from.y > to.y) {
                    return DOWN;
                }
            }

            if (from.x > to.x) {
                if (from.y > to.y) {
                    return DOWN;
                } else if (from.y < to.y) {
                    return UP;
                } else {
                    return UP;
                }
            }
        } else if (currentDirection.equals(UP)) {
            if (from.x == to.x) {
                if (from.y < to.y) {
                    return currentDirection;
                }
            }

            if (from.y == to.y) {
                if (from.x > to.x) {
                    return LEFT;
                }
                if (from.x < to.x) {
                    return RIGHT;
                }
            }

            if (from.y > to.y) {
                if (from.x > to.x) {
                    return LEFT;
                } else if (from.x < to.x) {
                    return RIGHT;
                } else {
                    return RIGHT;
                }
            }
        } else {
            if (from.x == to.x) {
                if (from.y > to.y) {
                    return currentDirection;
                }
            }

            if (from.y == to.y) {
                if (from.x > to.x) {
                    return LEFT;
                }
                if (from.x < to.x) {
                    return RIGHT;
                }
            }

            if (from.y < to.y) {
                if (from.x > to.x) {
                    return LEFT;
                } else if (from.x < to.x) {
                    return RIGHT;
                } else {
                    return RIGHT;
                }
            }
        }
        return currentDirection;
    }

    public Set<String> getAll() {
        Set<String> result = new HashSet<String>();
        result.addAll(Arrays.asList(LEFT, RIGHT, UP,  DOWN));
        return result;
    }
}
