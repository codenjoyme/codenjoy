package com.codenjoy.dojo.pong.model;

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
    public List<Hero> getHero() {
        List<Hero> result = new LinkedList<Hero>();

        for (Point pt : getPointsOf(Elements.HERO)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<>();

        for (Point pt : getPointsOf(Elements.VERTICAL_WALL)) {
            result.add(new Wall(pt, BarrierOrientation.VERTICAL));
        }

        for (Point pt : getPointsOf(Elements.HORIZONTAL_WALL)) {
            result.add(new Wall(pt, BarrierOrientation.HORISONTAL));
        }

        return result;
    }

    @Override
    public Ball getBall() {
        return new Ball(getPointsOf(Elements.BALL).get(0));
    }

    @Override
    public List<Panel> getPanels() {
        List<Panel> result = new LinkedList<>();
        for (Point pt : getPointsOf(Elements.PANEL)) {
            result.add(new Panel(pt, null));
        }

        return result;
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
