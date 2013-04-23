package com.apofig;

/**
 * User: oleksandr.baglai
 */
public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point pt) {
        this(pt.x, pt.y);
    }

    @Override
    public boolean equals(Object o) {
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
}