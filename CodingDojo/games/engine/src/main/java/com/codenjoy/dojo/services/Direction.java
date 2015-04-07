package com.codenjoy.dojo.services;

import java.util.Random;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Имплементит возможные направления движения чего либо
 */
public enum Direction {
    LEFT(0, -1, 0), RIGHT(1, 1, 0), UP(2, 0, -1), DOWN(3, 0, 1);

    private final int value;
    private final int dx;
    private final int dy;

    private Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
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
        return y - dy;
    }

    public Point change(Point point) {
        return pt(changeX(point.getX()), changeY(point.getY()));
    }

    public int value() {
        return value;
    }

    public Direction inverted() {
        switch (this) {
            case UP : return DOWN;
            case DOWN : return UP;
            case LEFT : return RIGHT;
            case RIGHT : return LEFT;
        }
        throw new IllegalArgumentException("Unsupported direction");
    }

    public static Direction random() {
        return Direction.valueOf(new Random().nextInt(4));
    }

    public Direction clockwise() {
        switch (this) {
            case LEFT: return UP;
            case UP: return RIGHT;
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
        }
        throw new IllegalArgumentException("Cant clockwise for: " + this);
    }
}
