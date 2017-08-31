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
 * Имплементит возможные направления движения чего либо во все 4 стороны включая 4 диагональные
 */
public enum DoubleDirection { // TODO test expansion
    LEFT(0, -1, 0), RIGHT(1, 1, 0), UP(2, 0, -1), DOWN(3, 0, 1),
    LEFT_UP(4, -1, -1), RIGHT_UP(5, 1, -1), LEFT_DOWN(6, -1, 1), RIGHT_DOWN(7, 1, 1),
    NONE(8, 0, 0);

    private final int value;
    private final int dx;
    private final int dy;

    private DoubleDirection(int value, int dx, int dy) {
        this.value = value;
        this.dx = dx;
        this.dy = dy;
    }

    public static DoubleDirection valueOf(int i) {
        for (DoubleDirection d : DoubleDirection.values()) {
            if (d.value == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such DoubleDirection for " + i);
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

    public DoubleDirection inverted() {
        switch (this) {
            case UP: return DOWN;
            case DOWN: return UP;
            case LEFT: return RIGHT;
            case RIGHT: return LEFT;
            case LEFT_DOWN: return RIGHT_UP;
            case LEFT_UP: return RIGHT_DOWN;
            case RIGHT_DOWN: return LEFT_UP;
            case RIGHT_UP: return LEFT_DOWN;
            case NONE: return NONE;
        }
        throw new IllegalArgumentException("Unsupported DoubleDirection");
    }

    public static DoubleDirection random() {
        return DoubleDirection.valueOf(new Random().nextInt(8));
    }

    public DoubleDirection clockwise() {
        switch (this) {
            case LEFT: return LEFT_UP;
            case LEFT_UP: return UP;
            case UP: return RIGHT_UP;
            case RIGHT_UP: return RIGHT;
            case RIGHT: return RIGHT_DOWN;
            case RIGHT_DOWN: return DOWN;
            case DOWN: return LEFT_DOWN;
            case LEFT_DOWN: return LEFT;
        }
        throw new IllegalArgumentException("Cant clockwise for DoubleDirection: " + this);
    }

    @Override
    public String toString() {
        return name().toUpperCase();
    }
}
