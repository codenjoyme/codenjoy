package com.codenjoy.dojo.battlecity.model.levels;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:30
 */
public class DefaultBorders {

    private final LinkedList<Point> result;
    private int size;

    public DefaultBorders(int size) {
        this.size = size;
        result = new LinkedList<Point>();
        addHorizontal();
        addVertical();
    }

    public List<Point> get() {
        return result;
    }

    private void addHorizontal() {
        for (int colNumber = 0; colNumber < size; colNumber++) {
            result.add(new PointImpl(0, colNumber));
            result.add(new PointImpl(size - 1, colNumber));
        }
    }

    private void addVertical() {
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            result.add(new PointImpl(rowNumber, 0));
            result.add(new PointImpl(rowNumber, size - 1));
        }
    }
}
