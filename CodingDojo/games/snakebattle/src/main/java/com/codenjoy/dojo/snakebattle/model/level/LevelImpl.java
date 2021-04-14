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
import com.codenjoy.dojo.snakebattle.model.board.Field;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.objects.*;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.snakebattle.model.Elements.*;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public Hero getHero(Field field) {
        Point point = LevelUtils.getObjects(xy, map, 
                pt -> pt,
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

        return parseSnake(point, field);
    }

    private Hero parseSnake(Point head, Field field) {
        Direction direction = getDirection(head);
        Hero hero = new Hero(direction);
        hero.init(field);

        Elements headElement = getAt(head);
        if (Arrays.asList(HEAD_FLY, ENEMY_HEAD_FLY).contains(headElement)) {
            direction = getHeadDirectionWithMod(head);
            hero.setDirection(direction);
            hero.eatFlying();
        }

        if (Arrays.asList(HEAD_EVIL, ENEMY_HEAD_EVIL).contains(headElement)) {
            direction = getHeadDirectionWithMod(head);
            hero.setDirection(direction);
            hero.eatFury();
        }

        hero.addTail(head);
        direction = direction.inverted();

        Point point = head;
        while (direction != null) {
            point = direction.change(point);
            hero.addTail(point);
            direction = next(point, direction);
        }

        return hero;
    }

    private Direction getHeadDirectionWithMod(Point head) {
        Elements atLeft = getAt(LEFT.change(head));
        if (Arrays.asList(Elements.BODY_HORIZONTAL,
                Elements.BODY_RIGHT_DOWN,
                Elements.BODY_RIGHT_UP,
                Elements.TAIL_END_LEFT,
                Elements.ENEMY_BODY_HORIZONTAL,
                Elements.ENEMY_BODY_RIGHT_DOWN,
                Elements.ENEMY_BODY_RIGHT_UP,
                Elements.ENEMY_TAIL_END_LEFT).contains(atLeft))
        {
            return RIGHT;
        }

        Elements atRight = getAt(RIGHT.change(head));
        if (Arrays.asList(Elements.BODY_HORIZONTAL,
                Elements.BODY_LEFT_DOWN,
                Elements.BODY_LEFT_UP,
                Elements.TAIL_END_RIGHT,
                Elements.ENEMY_BODY_HORIZONTAL,
                Elements.ENEMY_BODY_LEFT_DOWN,
                Elements.ENEMY_BODY_LEFT_UP,
                Elements.ENEMY_TAIL_END_RIGHT).contains(atRight))
        {
            return LEFT;
        }

        Elements atDown = getAt(DOWN.change(head));
        if (Arrays.asList(Elements.BODY_VERTICAL,
                Elements.BODY_LEFT_UP,
                Elements.BODY_RIGHT_UP,
                Elements.TAIL_END_DOWN,
                Elements.ENEMY_BODY_VERTICAL,
                Elements.ENEMY_BODY_LEFT_UP,
                Elements.ENEMY_BODY_RIGHT_UP,
                Elements.ENEMY_TAIL_END_DOWN).contains(atDown))
        {
            return UP;
        }

        Elements atUp = getAt(UP.change(head));
        if (Arrays.asList(Elements.BODY_VERTICAL,
                Elements.BODY_LEFT_DOWN,
                Elements.BODY_RIGHT_DOWN,
                Elements.TAIL_END_UP,
                Elements.ENEMY_BODY_VERTICAL,
                Elements.ENEMY_BODY_LEFT_DOWN,
                Elements.ENEMY_BODY_RIGHT_DOWN,
                Elements.ENEMY_TAIL_END_UP).contains(atUp))
        {
            return DOWN;
        }

        throw new RuntimeException("Smth wrong with head");
    }

    private Direction next(Point point, Direction direction) {
        switch (getAt(point)) {
            case BODY_HORIZONTAL:
            case ENEMY_BODY_HORIZONTAL:
                return direction;
            case BODY_VERTICAL:
            case ENEMY_BODY_VERTICAL:
                return direction;
            case BODY_LEFT_DOWN:
            case ENEMY_BODY_LEFT_DOWN:
                return ((direction == RIGHT) ? DOWN : LEFT);
            case BODY_RIGHT_DOWN:
            case ENEMY_BODY_RIGHT_DOWN:
                return ((direction == LEFT) ? DOWN : RIGHT);
            case BODY_LEFT_UP:
            case ENEMY_BODY_LEFT_UP:
                return ((direction == RIGHT) ? UP : LEFT);
            case BODY_RIGHT_UP:
            case ENEMY_BODY_RIGHT_UP:
                return ((direction == LEFT) ? UP : RIGHT);
        }
        return null;
    }

    @Override
    public Hero getEnemy(Field field) {
        Point point = LevelUtils.getObjects(xy, map, 
                pt -> pt,
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

        return parseSnake(point, field);
    }

    private Direction getDirection(Point point) {
        switch (getAt(point)) {
            case HEAD_DOWN :       return DOWN;
            case ENEMY_HEAD_DOWN : return DOWN;
            case HEAD_UP :         return UP;
            case ENEMY_HEAD_UP :   return UP;
            case HEAD_LEFT :       return LEFT;
            case ENEMY_HEAD_LEFT : return LEFT;
            default :              return RIGHT;
        }
    }

    @Override
    public List<Apple> getApples() {
        return LevelUtils.getObjects(xy, map, 
                pt -> new Apple(pt),
                APPLE);
    }

    @Override
    public List<Stone> getStones() {
        return LevelUtils.getObjects(xy, map,
                pt -> new Stone(pt),
                STONE);
    }

    @Override
    public List<FlyingPill> getFlyingPills() {
        return LevelUtils.getObjects(xy, map,
                pt -> new FlyingPill(pt),
                FLYING_PILL);
    }

    @Override
    public List<FuryPill> getFuryPills() {
        return LevelUtils.getObjects(xy, map,
                pt -> new FuryPill(pt),
                FURY_PILL);
    }

    @Override
    public List<Gold> getGold() {
        return LevelUtils.getObjects(xy, map,
                pt -> new Gold(pt),
                GOLD);
    }

    @Override
    public List<Wall> getWalls() {
        return LevelUtils.getObjects(xy, map,
                pt -> new Wall(pt),
                WALL);
    }

    @Override
    public List<StartFloor> getStartPoints() {
        return LevelUtils.getObjects(xy, map,
                pt -> new StartFloor(pt),
                START_FLOOR);
    }

    private Elements getAt(Point pt) {
        return Elements.valueOf(map.charAt(xy.getLength(pt.getX(), pt.getY())));
    }
}
