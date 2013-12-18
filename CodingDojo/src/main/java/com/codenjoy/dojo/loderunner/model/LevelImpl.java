package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:59
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
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.HERO_LEFT.getChar()) {
                result.add(new Hero(xy.getXY(index), Direction.LEFT));
            } else if (map.charAt(index) == Elements.HERO_RIGHT.getChar()) {
                result.add(new Hero(xy.getXY(index), Direction.RIGHT));
            }
        }
        return result;
    }

    @Override
    public List<Brick> getBricks() {
        List<Brick> result = new LinkedList<Brick>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.BRICK.getChar()) {
                result.add(new Brick(xy.getXY(index)));
            }
        }
        return result;
    }

    @Override
    public List<Point> getBorders() {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.UNDESTROYABLE_WALL.getChar()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

    @Override
    public List<Point> getGold() {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.GOLD.getChar()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
}
