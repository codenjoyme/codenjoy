package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:08 PM
 */
public class Point {
    protected int x;
    protected int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point point) {
        this(point.x, point.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean itsMe(Point pt) {
        return itsMe(pt.x, pt.y);
    }

    public boolean itsMe(int x, int y) {
        return this.x == x && this.y == y;
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

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
