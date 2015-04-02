package com.codenjoy.dojo.loderunner.client.utils;

/**
 * User: oleksandr.baglai
 */
public class Point implements Comparable<Point> {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point pt) {
        this(pt.x, pt.y);
    }

    public static Point pt(int x, int y) {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Point)) {
            return false;
        }

        Point pt = (Point)o;
        return pt.x == x && pt.y == y;
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", x, y);
    }

    public boolean isBad(int boardSize) {
        return x >= boardSize || y >= boardSize || x < 0 || y < 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int compareTo(Point o) {
        if (o == null) {
            return -1;
        }
        return Integer.valueOf(this.hashCode()).compareTo(Integer.valueOf(o.hashCode()));
    }

    @Override
    public int hashCode() {
        return x*1000 + y;
    }

}