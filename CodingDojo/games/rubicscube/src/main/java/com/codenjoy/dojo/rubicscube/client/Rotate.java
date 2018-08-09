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

public enum Rotate {
    CLOCKWISE(1),
    TWICE(2),
    CONTR_CLOCKWISE(-1);

    private final int rotate;

    private Rotate(int rotate) {
        this.rotate = rotate;
    }

    public String toString() {
        return this.name();
    }

    public static Rotate valueOf(int i) {
        for (Rotate d : Rotate.values()) {
            if (d.rotate == i) {
                return d;
            }
        }
        throw new IllegalArgumentException("No such Rotate for " + i);
    }

    public int rotate() {
        return rotate;
    }

    public static Rotate random(Dice dice) {
        Rotate[] values = Rotate.values();
        return values[dice.next(values.length)];
    }
}
