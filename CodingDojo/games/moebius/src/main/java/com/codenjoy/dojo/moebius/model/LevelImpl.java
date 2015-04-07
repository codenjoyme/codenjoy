package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.Arrays;
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
    public List<Line> getLines() {
        List<Line> result = new LinkedList<Line>();

        for (int index = 0; index < map.length(); index++) {
            for (Elements el : Arrays.asList(Elements.LEFT_UP,
                    Elements.UP_RIGHT,
                    Elements.RIGHT_DOWN,
                    Elements.DOWN_LEFT,
                    Elements.LEFT_RIGHT,
                    Elements.UP_DOWN,
                    Elements.CROSS)) {
                if (map.charAt(index) == el.ch()) {
                    Point pt = xy.getXY(index);
                    result.add(new Line(pt, el));
                }
            }
        }

        return result;
    }
}
