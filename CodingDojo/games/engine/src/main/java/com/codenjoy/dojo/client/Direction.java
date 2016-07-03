package com.codenjoy.dojo.client;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Arrays;
import java.util.Random;


/**
 * Этот enum содержит все возможные команды и дополнительные методы для работы с ними.
 * Сервер ожидает одну из этих команд, либо комбинацию из двух команд, разделенных запятыми.
 * Команда ACT может иметь любьое число целочисленных параметров, а может и не иметь их - зависит от игры.
 */
// TODO подумать как избавиться в пользу того, что есть в пакете server
// TODO тут главное отличие верх-низ - низ-верх @see changeY()
public enum Direction {
    UP(2, 0, -1), DOWN(3, 0, 1), LEFT(0, -1, 0), RIGHT(1, 1, 0),
    ACT(4, 0, 0),
    STOP(5, 0, 0);

    private final int value;
    private final int dx;
    private final int dy;

    Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public String toString() {
        return this.name();
    }

    public static Direction valueOf(int i) {
        for (Direction d : Direction.values()) {
            if (d.value == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Direction for " + i);
    }

    public int changeX(int x) {
        return x + dx;
    }

    public int changeY(int y) {
        return y + dy;
    }

    public Direction inverted() {
        switch (this) {
            case UP : return DOWN;
            case DOWN : return UP;
            case LEFT : return RIGHT;
            case RIGHT : return LEFT;
            default : return STOP;
        }
    }

    public Point change(Point point) {
        return new PointImpl(changeX(point.getX()), changeY(point.getY()));
    }

    public int value() {
        return value;
    }

    public static Direction random() {
        return random(new RandomDice());
    }

    public static Direction random(Dice dice) {
        return Direction.valueOf(dice.next(4));
    }

    public Direction clockwise() {
        switch (this) {
            case LEFT: return UP;
            case UP: return RIGHT;
            case RIGHT: return DOWN;
            case DOWN: return LEFT;
        }
        throw new IllegalArgumentException("Cant clockwise for: " + this);
    }

    public static String ACT(int... parameters) {
        String s = Arrays.toString(parameters).replaceAll("[\\[\\] ]", "");
        return ACT.toString() + "(" + s + ")";
    }

    public String ACT(boolean before) {
        if (before) {
            return "ACT," + toString();
        } else {
            return toString() + ",ACT";
        }
    }
}
