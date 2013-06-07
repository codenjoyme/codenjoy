package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:08 PM
 */
public class Point implements IPoint {
    protected int x;
    protected int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(IPoint point) {
        this(point.getX(), point.getY());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean itsMe(IPoint pt) {
        return itsMe(pt.getX(), pt.getY());
    }

    public boolean itsMe(int x, int y) {
        return this.x == x && this.y == y;
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

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
