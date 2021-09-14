package com.codenjoy.dojo.snakebattle.model;

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


import com.codenjoy.dojo.games.snakebattle.Element;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.snakebattle.model.board.Field;
import com.codenjoy.dojo.snakebattle.model.hero.Hero;
import com.codenjoy.dojo.snakebattle.model.objects.*;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.games.snakebattle.Element.*;
import static com.codenjoy.dojo.services.Direction.*;
import static java.util.function.Function.identity;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public Hero hero(Field field) {
        Point point = find(identity(),
                HEAD_DOWN,
                HEAD_UP,
                HEAD_LEFT,
                HEAD_RIGHT,
                HEAD_SLEEP,
                HEAD_DEAD,
                HEAD_EVIL,
                HEAD_FLY).stream()
                        .findAny()
                        .orElse(null);

        if (point == null) {
            return null;
        }

        return parseSnake(point, field);
    }

    private Hero parseSnake(Point head, Field field) {
        Direction direction = direction(head);
        Hero hero = new Hero(direction);
        hero.init(field);

        Element headElement = at(head);
        if (Arrays.asList(HEAD_FLY, ENEMY_HEAD_FLY).contains(headElement)) {
            direction = headDirectionWithMod(head);
            hero.setDirection(direction);
            hero.eatFlying();
        }

        if (Arrays.asList(HEAD_EVIL, ENEMY_HEAD_EVIL).contains(headElement)) {
            direction = headDirectionWithMod(head);
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

    private Direction headDirectionWithMod(Point head) {
        Element atLeft = at(LEFT.change(head));
        if (Arrays.asList(Element.BODY_HORIZONTAL,
                Element.BODY_RIGHT_DOWN,
                Element.BODY_RIGHT_UP,
                Element.TAIL_END_LEFT,
                Element.ENEMY_BODY_HORIZONTAL,
                Element.ENEMY_BODY_RIGHT_DOWN,
                Element.ENEMY_BODY_RIGHT_UP,
                Element.ENEMY_TAIL_END_LEFT).contains(atLeft))
        {
            return RIGHT;
        }

        Element atRight = at(RIGHT.change(head));
        if (Arrays.asList(Element.BODY_HORIZONTAL,
                Element.BODY_LEFT_DOWN,
                Element.BODY_LEFT_UP,
                Element.TAIL_END_RIGHT,
                Element.ENEMY_BODY_HORIZONTAL,
                Element.ENEMY_BODY_LEFT_DOWN,
                Element.ENEMY_BODY_LEFT_UP,
                Element.ENEMY_TAIL_END_RIGHT).contains(atRight))
        {
            return LEFT;
        }

        Element atDown = at(DOWN.change(head));
        if (Arrays.asList(Element.BODY_VERTICAL,
                Element.BODY_LEFT_UP,
                Element.BODY_RIGHT_UP,
                Element.TAIL_END_DOWN,
                Element.ENEMY_BODY_VERTICAL,
                Element.ENEMY_BODY_LEFT_UP,
                Element.ENEMY_BODY_RIGHT_UP,
                Element.ENEMY_TAIL_END_DOWN).contains(atDown))
        {
            return UP;
        }

        Element atUp = at(UP.change(head));
        if (Arrays.asList(Element.BODY_VERTICAL,
                Element.BODY_LEFT_DOWN,
                Element.BODY_RIGHT_DOWN,
                Element.TAIL_END_UP,
                Element.ENEMY_BODY_VERTICAL,
                Element.ENEMY_BODY_LEFT_DOWN,
                Element.ENEMY_BODY_RIGHT_DOWN,
                Element.ENEMY_TAIL_END_UP).contains(atUp))
        {
            return DOWN;
        }

        throw new RuntimeException("Smth wrong with head");
    }

    private Direction next(Point point, Direction direction) {
        switch (at(point)) {
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

    public Hero enemy(Field field) {
        Point point = find(
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

    private Direction direction(Point point) {
        switch (at(point)) {
            case HEAD_DOWN :       return DOWN;
            case ENEMY_HEAD_DOWN : return DOWN;
            case HEAD_UP :         return UP;
            case ENEMY_HEAD_UP :   return UP;
            case HEAD_LEFT :       return LEFT;
            case ENEMY_HEAD_LEFT : return LEFT;
            default :              return RIGHT;
        }
    }

    public List<Apple> apples() {
        return find(Apple::new, APPLE);
    }

    public List<Stone> stones() {
        return find(Stone::new, STONE);
    }

    public List<FlyingPill> flyingPills() {
        return find(FlyingPill::new, FLYING_PILL);
    }

    public List<FuryPill> furyPills() {
        return find(FuryPill::new, FURY_PILL);
    }

    public List<Gold> gold() {
        return find(Gold::new, GOLD);
    }

    public List<Wall> walls() {
        return find(Wall::new, WALL);
    }

    public List<StartFloor> startPoints() {
        return find(StartFloor::new, START_FLOOR);
    }

    private Element at(Point pt) {
        return Element.valueOf(map.charAt(xy.getLength(pt.getX(), pt.getY())));
    }
}
