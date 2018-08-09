package com.codenjoy.dojo.rubicscube.client;

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


import com.codenjoy.dojo.services.Dice;

public enum Face {
    LEFT(1),
    FRONT(2),
    RIGHT(3),
    BACK(4),
    UP(5),
    DOWN(6);

    private final int number;

    private Face(int number) {
        this.number = number;
    }

    public String toString() {
        return this.name();
    }

    public int number() {
        return number;
    }

    public static Face valueOf(int i) {
        for (Face d : Face.values()) {
            if (d.number == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Face for " + i);
    }

    public static Face random(Dice dice) {
        return Face.valueOf(dice.next(Face.values().length) + 1);
    }
}
