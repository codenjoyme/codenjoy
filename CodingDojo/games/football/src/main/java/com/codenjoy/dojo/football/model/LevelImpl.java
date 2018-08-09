package com.codenjoy.dojo.football.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.football.model.elements.Goal;
import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.model.elements.Wall;
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
    public List<Hero> getHero() {
        List<Hero> result = new LinkedList<>();

        for (Point pt : getPointsOf(Elements.HERO)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.HERO_W_BALL)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.TEAM_MEMBER)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.TEAM_MEMBER_W_BALL)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.ENEMY)) {
            result.add(new Hero(pt));
        }
        
        for (Point pt : getPointsOf(Elements.ENEMY_W_BALL)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<>();

        for (Point pt : getPointsOf(Elements.WALL)) {
            result.add(new Wall(pt));
        }

        return result;
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
    
    private List<Point> getPointsOf(Elements... elements) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            for (Elements element : elements) {
                if (map.charAt(index) == element.ch) {
                    result.add(xy.getXY(index));
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<Ball> getBalls() {
        List<Ball> result = new LinkedList<>();

        for (Point pt : getPointsOf(Elements.BALL, 
                                    Elements.STOPPED_BALL, 
                                    Elements.HERO_W_BALL, 
                                    Elements.TEAM_MEMBER_W_BALL,
                                    Elements.ENEMY_W_BALL, 
                                    Elements.HITED_GOAL,
                                    Elements.HITED_MY_GOAL
                                    )) {
            result.add(new Ball(pt));
        }
        
        return result;
    }

    @Override
    public List<Goal> getTopGoals() {
        List<Goal> result = new LinkedList<>();

        for (Point pt : getPointsOf(Elements.TOP_GOAL)) {
            result.add(new Goal(pt, Elements.TOP_GOAL));
        }

        return result;
    }

    @Override
    public List<Goal> getBottomGoals() {
        List<Goal> result = new LinkedList<>();

        for (Point pt : getPointsOf(Elements.BOTTOM_GOAL)) {
            result.add(new Goal(pt, Elements.BOTTOM_GOAL));
        }

        return result;
    }
}
