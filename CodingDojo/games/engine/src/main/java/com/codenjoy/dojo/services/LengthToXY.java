package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:17
 */
public class LengthToXY { // TODO test me
    private int size;

    public LengthToXY(int size) {
        this.size = size;
    }

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return new PointImpl(length % size, size - 1 - length / size);
    }

    public int getLength(int x, int y) {
        return (size - 1 - y) * size + x;
    }
}