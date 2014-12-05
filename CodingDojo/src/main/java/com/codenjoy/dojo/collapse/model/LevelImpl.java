package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> result = new LinkedList<Cell>();
        for (int index = 0; index < map.length(); index++) {
            char ch = map.charAt(index);
            if (ch != Elements.BORDER.ch && ch != ' ') {
                result.add(new Cell(xy.getXY(index), Integer.valueOf("" + ch)));
            }
        }

        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<Wall>();
        for (int index = 0; index < map.length(); index++) {
            char ch = map.charAt(index);
            if (ch == Elements.BORDER.ch) {
                result.add(new Wall(xy.getXY(index)));
            }
        }
        return result;
    }
}
