package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

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
        return new PointImpl(length % size, length / size);
    }

    public int getLength(int x, int y) {
        return (y)* size + x;
    }
}