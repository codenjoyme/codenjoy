package com.codenjoy.dojo.battlecity.model;

/**
 * User: sanja
 * Date: 06.06.13
 * Time: 17:30
 */
public class Point {  // TODO remove duplicate with other games
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

    @Override
    public int hashCode() {
        return x*1000 + y;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Point)) {
            return false;
        }

        Point p = (Point)o;

        return (p.x == x && p.y == y);
    }
}
