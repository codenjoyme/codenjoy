package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.LengthToXY;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;
    private String mask;

    public LevelImpl(String map, String mask) {
        this.map = map;
        this.mask = mask;

        if (map.length() != mask.length()) {
            throw new IllegalArgumentException("Маска не совпадает с полем по размеhу: " +
                    map.length() + "-" + mask.length());
        }

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
            char mch = mask.charAt(index);
            if (ch != Elements.BORDER.ch) {
                result.add(new Cell(xy.getXY(index), Integer.valueOf("" + ch), mch != '?'));
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
