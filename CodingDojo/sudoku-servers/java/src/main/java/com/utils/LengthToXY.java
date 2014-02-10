package com.utils;

/**
 * User: oleksandr.baglai
 */
public class LengthToXY {
    private int size;

    public LengthToXY(int size) {
        this.size = size;
    }

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return new Point(length % size + 1, length / size + 1);
    }

    public int getLength(int x, int y) {
        x--;
        y--;
        return (size - y - 1)* size + x;
    }
}