package com.codenjoy.dojo.sample.model;

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
    public List<Robot> getRobots(Field field) {
        List<Robot> result = new LinkedList<Robot>();

        for (Point pt : getPointsOf(Elements.ROBOT)) {
            result.add(new Robot(field, pt.getX(), pt.getY()));
        }

        return result;
    }

    public List<Hero> getOtherHero() {
        List<Hero> result = new LinkedList<Hero>();

        for (Point pt : getPointsOf(Elements.OTHER_HERO)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Ability> getAbility() {
        List<Ability> result = new LinkedList<Ability>();

        for (Point pt : getPointsOf(Elements.SUPER_ATTACK)) {
            result.add(new Ability(pt, Ability.Type.WEAPON));
        }
        for (Point pt : getPointsOf(Elements.SUPER_DEFENCE)) {
             result.add(new  Ability(pt, Ability.Type.DEFENCE));
        }
        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<Wall>();

        for (Point pt : getPointsOf(Elements.WALL)) {
            result.add(new Wall(pt));
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
