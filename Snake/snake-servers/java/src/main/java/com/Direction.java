package com;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:06 AM
 */
public class Direction {

    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

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
            return (barrier.y == from.y && (from.x > barrier.x && barrier.x > to.x)) ||
                    ((from.x == to.x) && barrier.x == from.x - 1);
        } else if (RIGHT.equals(direction)) {
            return (barrier.y == from.y && (from.x < barrier.x && barrier.x < to.x)) ||
                    ((from.x == to.x) && barrier.x == from.x + 1);
        } else if (DOWN.equals(direction)) {
            return (barrier.x == from.x && (from.y > barrier.y && barrier.y > to.y)) ||
                    ((from.y == to.y) && barrier.y == from.y - 1);
        } else  {
            return (barrier.x == from.x &&(from.y < barrier.y && barrier.y < to.y)) ||
                    ((from.y == to.y) && barrier.y == from.y + 1);
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
        directions.remove(inverted(currentDirection));

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
        if (direction.equals(Direction.LEFT)) {
            return Direction.UP;
        } else if (direction.equals(Direction.UP)) {
            return Direction.RIGHT;
        } else if (direction.equals(Direction.RIGHT)) {
            return Direction.DOWN;
        } else {
            return Direction.LEFT;
        }
    }

    public String get(List<Point> barriers) {
        if (currentDirection.equals(LEFT)) {
            if (from.y == to.y) {
                if (from.x > to.x) {
                    return getPossibleDirection(barriers, currentDirection);
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
                    return getPossibleDirection(barriers, currentDirection);
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
                    return getPossibleDirection(barriers, currentDirection);
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
                    return getPossibleDirection(barriers, currentDirection);
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
