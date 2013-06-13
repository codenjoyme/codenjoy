package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:08 PM
 */
public class PointImpl implements Point {
    protected int x;
    protected int y;

    public PointImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointImpl(Point point) {
        this(point.getX(), point.getY());
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public boolean itsMe(Point pt) {
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

        if (!(o instanceof PointImpl)) {
            return false;
        }

        PointImpl p = (PointImpl)o;

        return (p.x == x && p.y == y);
    }

    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PointImpl copy() {
        return new PointImpl(this);
    }

    public void change(Point delta) {
        x += delta.getX();
        y += delta.getY();
    }
}
