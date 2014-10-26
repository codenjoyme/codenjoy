package com.codenjoy.dojo.hex.model;

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
    public List<Hero> getHeroes() {
        List<Hero> result = new LinkedList<Hero>();

        for (Point pt : getPointsOf(Elements.HERO1)) {
            result.add(new Hero(pt));
        }

        for (Point pt : getPointsOf(Elements.HERO2)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Point> getWalls() {
        return getPointsOf(Elements.WALL);
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
}
