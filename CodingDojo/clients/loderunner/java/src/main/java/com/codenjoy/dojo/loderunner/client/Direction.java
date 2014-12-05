package com.codenjoy.dojo.loderunner.client;

import com.codenjoy.dojo.loderunner.client.utils.Point;

/**
 * User: oleksandr.baglai
 */
public enum Direction {
    UP(2, 0, -1), DOWN(3, 0, 1), LEFT(0, -1, 0), RIGHT(1, 1, 0),  // direction of Bomberman
    ACT(4, 0, 0),                                                 // drop a bomb
    STOP(5, 0, 0);                                                // stop the Bomberman

    final int value;
    private final int dx;
    private final int dy;

    Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public String toString() {
        return this.name();
    }

    public static Direction valueOf(int i) {
        for (Direction d : Direction.values()) {
            if (d.value == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Direction for " + i);
    }

    public int changeX(int x) {
        return x + dx;
    }


    public int changeY(int y) {
        return y + dy;
    }

    public Direction inverted() {
        switch (this) {
            case UP : return DOWN;
            case DOWN : return UP;
            case LEFT : return RIGHT;
            case RIGHT : return LEFT;
            default : return STOP;
        }
    }

    public Point change(Point point) {
        return new Point(changeX(point.getX()), changeY(point.getY()));
    }
}
