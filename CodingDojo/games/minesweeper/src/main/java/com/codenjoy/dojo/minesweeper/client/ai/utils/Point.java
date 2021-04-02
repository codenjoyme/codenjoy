//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.codenjoy.dojo.minesweeper.client.ai.utils;

public class Point {
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

    public double distance(Point other) {
        return Math.sqrt((double)((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y)));
    }

    public boolean equals(Object o) {
        Point pt = (Point)o;
        return pt.x == this.x && pt.y == this.y;
    }

    public String toString() {
        return String.format("[%s,%s]", this.x, this.y);
    }

    public boolean isBad(int boardSize) {
        return this.x >= boardSize || this.y >= boardSize || this.x < 0 || this.y < 0;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
