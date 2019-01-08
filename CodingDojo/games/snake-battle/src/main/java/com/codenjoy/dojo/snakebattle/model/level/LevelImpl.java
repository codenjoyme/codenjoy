package com.codenjoy.dojo.snakebattle.model.level;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snakebattle.model.Elements;
import com.codenjoy.dojo.snakebattle.model.objects.*;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.snakebattle.model.Elements.*;
import static com.codenjoy.dojo.snakebattle.model.Elements.ENEMY_HEAD_SLEEP;
import static java.util.stream.Collectors.toList;

public class LevelImpl implements Level {

    private LengthToXY xy;
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
    public Hero getHero() {
        Point point = getPointsOf(
                HEAD_DOWN,
                HEAD_UP,
                HEAD_LEFT,
                HEAD_RIGHT,
                HEAD_SLEEP,
                HEAD_DEAD,
                HEAD_EVIL,
                HEAD_FLY)
                .stream()
                .findAny()
                .orElse(null);

        if (point == null) {
            return null;
        }

        Direction direction = getDirection(point);

        Hero hero = new Hero(direction);

        List<Point> tail = getPointsOf(
                TAIL_END_DOWN,
                TAIL_END_LEFT,
                TAIL_END_UP,
                TAIL_END_RIGHT,
                TAIL_INACTIVE,
                BODY_HORIZONTAL,
                BODY_VERTICAL,
                BODY_LEFT_DOWN,
                BODY_LEFT_UP,
                BODY_RIGHT_DOWN,
                BODY_RIGHT_UP);

        tail.add(point);
        hero.addTail(tail);

        return hero;
    }

    @Override
    public Hero getEnemy() {
        Point point = getPointsOf(
                ENEMY_HEAD_DOWN,
                ENEMY_HEAD_UP,
                ENEMY_HEAD_LEFT,
                ENEMY_HEAD_RIGHT,
                ENEMY_HEAD_SLEEP,
                ENEMY_HEAD_DEAD,
                ENEMY_HEAD_EVIL,
                ENEMY_HEAD_FLY)
                .stream()
                .findAny()
                .orElse(null);

        if (point == null) {
            return null;
        }

        Direction direction = getDirection(point);

        Hero hero = new Hero(direction);

        List<Point> tail = getPointsOf(
                ENEMY_TAIL_END_DOWN,
                ENEMY_TAIL_END_LEFT,
                ENEMY_TAIL_END_UP,
                ENEMY_TAIL_END_RIGHT,
                ENEMY_TAIL_INACTIVE,
                ENEMY_BODY_HORIZONTAL,
                ENEMY_BODY_VERTICAL,
                ENEMY_BODY_LEFT_DOWN,
                ENEMY_BODY_LEFT_UP,
                ENEMY_BODY_RIGHT_DOWN,
                ENEMY_BODY_RIGHT_UP);

        tail.add(point);
        hero.addTail(tail);

        return hero;
    }

    private Direction getDirection(Point point) {
        switch (getAt(point)) {
            case HEAD_DOWN : return Direction.DOWN;
            case ENEMY_HEAD_DOWN : return Direction.DOWN;
            case HEAD_UP : return Direction.UP;
            case ENEMY_HEAD_UP : return Direction.UP;
            case HEAD_LEFT : return Direction.LEFT;
            case ENEMY_HEAD_LEFT : return Direction.LEFT;
            default : return Direction.RIGHT;
        }
    }

    @Override
    public List<Apple> getApples() {
        return getPointsOf(APPLE).stream()
                .map(Apple::new)
                .collect(toList());
    }

    @Override
    public List<Stone> getStones() {
        return getPointsOf(STONE).stream()
                .map(Stone::new)
                .collect(toList());
    }

    @Override
    public List<FlyingPill> getFlyingPills() {
        return getPointsOf(FLYING_PILL).stream()
                .map(FlyingPill::new)
                .collect(toList());
    }

    @Override
    public List<FuryPill> getFuryPills() {
        return getPointsOf(FURY_PILL).stream()
                .map(FuryPill::new)
                .collect(toList());
    }

    @Override
    public List<Gold> getGold() {
        return getPointsOf(GOLD).stream()
                .map(Gold::new)
                .collect(toList());
    }

    @Override
    public List<Wall> getWalls() {
        return getPointsOf(WALL).stream()
                .map(Wall::new)
                .collect(toList());
    }

    @Override
    public List<StartFloor> getStartPoints() {
        return getPointsOf(START_FLOOR).stream()
                .map(StartFloor::new)
                .collect(toList());
    }

    private List<Point> getPointsOf(Elements... elements) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            for (Elements element : elements) {
                if (map.charAt(index) == element.ch()) {
                    result.add(xy.getXY(index));
                }
            }
        }
        return result;
    }

    private Elements getAt(Point pt) {
        return Elements.valueOf(map.charAt(xy.getLength(pt.getX(), pt.getY())));
    }
}
