package com.codenjoy.dojo.services;

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


import java.util.Random;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Имплементит возможные направления движения чего либо
 */
public enum Direction {
    LEFT(0, -1, 0), RIGHT(1, 1, 0), UP(2, 0, -1), DOWN(3, 0, 1);

    private final int value;
    private final int dx;
    private final int dy;

    private Direction(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
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
        return y - dy;
    }

    public Point change(Point point) {
        return pt(changeX(point.getX()), changeY(point.getY()));
    }

    public int value() {
        return value;
    }

    public Direction inverted() {
        switch (this) {
            case UP : return DOWN;
            case DOWN : return UP;
            case LEFT : return RIGHT;
            case RIGHT : return LEFT;
        }
        throw new IllegalArgumentException("Unsupported direction");
    }

    public static Direction random() {
        return Direction.valueOf(new Random().nextInt(4));
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
}
