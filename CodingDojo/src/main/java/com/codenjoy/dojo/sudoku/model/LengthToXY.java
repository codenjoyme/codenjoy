package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * User: oleksandr.baglai
 */
public class LengthToXY {
    public LengthToXY(int size) {
        this.size = size;
    }

    private int size;

    public Point getXY(int length) {
        if (length == -1) {
            return null;
        }
        return PointImpl.pt(length % size + 1, length / size + 1);
    }

    public int getLength(int x, int y) {
        return (size - y - 1)* size + x;
    }

    public int getSize() {
        return size;
    }
}