package com.codenjoy.dojo.battlecity.model;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:30
 */
public class Point {
    protected int x;
    protected int y;

    public Point(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    public Point copy() {
        return new Point(x,  y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
