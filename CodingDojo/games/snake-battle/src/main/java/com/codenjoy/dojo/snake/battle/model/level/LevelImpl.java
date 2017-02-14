package com.codenjoy.dojo.snake.battle.model.level;

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


import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.battle.model.Elements;
import com.codenjoy.dojo.snake.battle.model.objects.*;
import com.codenjoy.dojo.snake.battle.model.hero.Hero;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.snake.battle.model.Elements.*;

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
        List<Hero> result = new LinkedList<>();
        addHeroesToList(result, HEAD_DOWN,
                HEAD_LEFT,
                HEAD_RIGHT,
                HEAD_UP,
                HEAD_SLEEP,
                HEAD_DEAD,
                HEAD_EVIL,
                HEAD_FLY);
        return result;
    }

    @Override
    public List<Hero> getEnemy() {
        List<Hero> result = new LinkedList<>();
        addHeroesToList(result, ENEMY_HEAD_DOWN,
                ENEMY_HEAD_LEFT,
                ENEMY_HEAD_RIGHT,
                ENEMY_HEAD_UP,
                ENEMY_HEAD_SLEEP,
                ENEMY_HEAD_DEAD,
                ENEMY_HEAD_EVIL,
                ENEMY_HEAD_FLY);
        return result;
    }

    private void addHeroesToList(List<Hero> list, Elements... elements) {
        for (Elements element : elements)
            for (Point pt : getPointsOf(element))
                list.add(new Hero(pt));
    }

    @Override
    public List<Apple> getApples() {
        List<Apple> result = new LinkedList<>();
        for (Point pt : getPointsOf(APPLE))
            result.add(new Apple(pt));
        return result;
    }

    @Override
    public List<Stone> getStones() {
        List<Stone> result = new LinkedList<>();
        for (Point pt : getPointsOf(STONE))
            result.add(new Stone(pt));

        return result;
    }

    @Override
    public List<FlyingPill> getFlyingPills() {
        List<FlyingPill> result = new LinkedList<>();
        for (Point pt : getPointsOf(FLYING_PILL))
            result.add(new FlyingPill(pt));
        return result;
    }

    @Override
    public List<FuryPill> getFuryPills() {
        List<FuryPill> result = new LinkedList<>();
        for (Point pt : getPointsOf(FURY_PILL))
            result.add(new FuryPill(pt));
        return result;
    }

    @Override
    public List<Gold> getGold() {
        List<Gold> result = new LinkedList<>();
        for (Point pt : getPointsOf(GOLD))
            result.add(new Gold(pt));
        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<>();
        for (Point pt : getPointsOf(WALL))
            result.add(new Wall(pt));
        return result;
    }

    @Override
    public List<StartFloor> getStartPoints() {
        List<StartFloor> result = new LinkedList<>();
        for (Point pt : getPointsOf(START_FLOOR))
            result.add(new StartFloor(pt));
        return result;
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
}
