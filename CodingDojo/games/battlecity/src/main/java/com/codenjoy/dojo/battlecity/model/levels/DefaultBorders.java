package com.codenjoy.dojo.battlecity.model.levels;

import com.codenjoy.dojo.battlecity.model.Border;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:30
 */
public class DefaultBorders {

    private final List<Border> result;
    private int size;

    public DefaultBorders(int size) {
        this.size = size;
        result = new LinkedList<Border>();
        addHorizontal();
        addVertical();
    }

    public List<Border> get() {
        return result;
    }

    private void addHorizontal() {
        for (int colNumber = 0; colNumber < size; colNumber++) {
            result.add(new Border(0, colNumber));
            result.add(new Border(size - 1, colNumber));
        }
    }

    private void addVertical() {
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            result.add(new Border(rowNumber, 0));
            result.add(new Border(rowNumber, size - 1));
        }
    }
}
