//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai;

public enum Direction {
    UP(2, 0, -1),
    DOWN(3, 0, 1),
    LEFT(0, -1, 0),
    RIGHT(1, 1, 0),
    ACT(4, 0, 0),
    STOP(5, 0, 0);

    final int value;
    private final int dx;
    private final int dy;

    private Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public String toString() {
        return this.name();
    }

    public static Direction valueOf(int i) {
        Direction[] arr$ = values();
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            Direction d = arr$[i$];
            if (d.value == i) {
                return d;
            }
        }

        throw new IllegalArgumentException("No such Direction for " + i);
    }

    public int changeX(int x) {
        return x + this.dx;
    }

    public int changeY(int y) {
        return y + this.dy;
    }

    public Direction inverted() {
        switch(this) {
        case UP:
            return DOWN;
        case DOWN:
            return UP;
        case LEFT:
            return RIGHT;
        case RIGHT:
            return LEFT;
        default:
            return STOP;
        }
    }
}
