package com.codenjoy.dojo.hex.client.utils;

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
        return new Point(length % size,  size - 1 - length / size);
    }

    public int getLength(int x, int y) {
        return (size - 1 - y)* size + x; // TODO привести hex к общим координатам - 0,0 - левый верхний угол
    }
}