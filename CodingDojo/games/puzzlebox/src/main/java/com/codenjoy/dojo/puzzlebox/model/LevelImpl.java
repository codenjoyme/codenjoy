package com.codenjoy.dojo.puzzlebox.model;

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Полезный утилитный класс для получения объектов на поле из текстового вида.
 */
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
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<Wall>();

        for (Point pt : getPointsOf(Elements.WALL)) {
            result.add(new Wall(pt));
        }

        return result;
    }

    @Override
    public List<Box> getBoxes() {
        List<Box> result = new LinkedList<Box>();

        for (Point pt : getPointsOf(Elements.BOX)) {
            result.add(new Box(pt));
        }

        return result;
    }


    @Override
    public List<Target> getTargets() {
        List<Target> result = new LinkedList<Target>();

        for (Point pt : getPointsOf(Elements.TARGET)) {
            result.add(new Target(pt));
        }

        return result;
    }

//    @Override
//    public List<Gold> getGold() {
//        List<Gold> result = new LinkedList<Gold>();
//
//        for (Point pt : getPointsOf(Elements.GOLD)) {
//            result.add(new Gold(pt));
//        }
//
//        return result;
//        return null; // TODO
//    }

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
