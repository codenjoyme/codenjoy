package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:08 PM
 */
public class Point {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean itsMe(int x, int y) {
        return this.x == x && this.y == y;
    }
}
