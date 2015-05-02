package com.codenjoy.dojo.services;

public class LengthToXY { // TODO есть точно такой же в com.codenjoy.dojo.client; только вертикально зеркальный
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