package com.codenjoy.dojo.loderunner.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


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

    EnemyAI ai;

    private final LengthToXY xy;
    private String map;

    public LevelImpl(String map) {
        this.map = map;
        ai = new ApofigAI();
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHeroes() {
        List<Hero> result = new LinkedList<Hero>();

        List<Point> points = getPointsOf(Elements.HERO_LEFT);
        for (Point pt : points) {
            result.add(new Hero(pt, Direction.LEFT));
        }

        points = getPointsOf(Elements.HERO_RIGHT);
        for (Point pt : points) {
            result.add(new Hero(pt, Direction.RIGHT));
        }

        return result;
    }

    @Override
    public List<Brick> getBricks() {
        List<Point> points = getPointsOf(Elements.BRICK);

        List<Brick> result = new LinkedList<Brick>();
        for (Point pt : points) {
            result.add(new Brick(pt));
        }
        return result;
    }

    @Override
    public List<Border> getBorders() {
        List<Border> result = new LinkedList<Border>();

        List<Point> points = getPointsOf(Elements.UNDESTROYABLE_WALL);
        for (Point pt : points) {
            result.add(new Border(pt));
        }
        return result;
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.getChar()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

    @Override
    public List<Gold> getGold() {
        List<Gold> result = new LinkedList<Gold>();

        List<Point> points = getPointsOf(Elements.GOLD);
        for (Point pt : points) {
            result.add(new Gold(pt));
        }
        return result;
    }

    @Override
    public List<Ladder> getLadder() {
        List<Ladder> result = new LinkedList<Ladder>();

        List<Point> points = getPointsOf(Elements.LADDER);
        for (Point pt : points) {
            result.add(new Ladder(pt));
        }
        return result;
    }

    @Override
    public List<Pipe> getPipe() {
        List<Pipe> result = new LinkedList<Pipe>();

        List<Point> points = getPointsOf(Elements.PIPE);
        for (Point pt : points) {
            result.add(new Pipe(pt));
        }
        return result;
    }

    @Override
    public List<Enemy> getEnemies() {
        List<Enemy> result = new LinkedList<Enemy>();

        List<Point> points = getPointsOf(Elements.ENEMY_RIGHT);
        for (Point pt : points) {
            result.add(new Enemy(pt, Direction.RIGHT, ai));
        }

        points = getPointsOf(Elements.ENEMY_LEFT);
        for (Point pt : points) {
            result.add(new Enemy(pt, Direction.LEFT, ai));
        }

        return result;
    }

    public void setAI(EnemyAI ai) {
        this.ai = ai;
    }
}
