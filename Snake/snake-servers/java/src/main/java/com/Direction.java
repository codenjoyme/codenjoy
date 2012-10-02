package com;

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

    public String get() {
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
        return "";
    }
}
