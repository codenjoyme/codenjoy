package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 10:35 PM
 */
public enum Direction {
    LEFT(0, -1, 0), RIGHT(1, 1, 0), UP(2, 0, -1), DOWN(3, 0, 1);

    final int value;
    private final int dx;
    private final int dy;

    Direction(int value, int dx, int dy) {
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
        return y + dy;
    }
}
