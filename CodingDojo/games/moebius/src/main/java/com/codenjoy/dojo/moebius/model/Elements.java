package com.codenjoy.dojo.moebius.model;

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


import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.PointImpl.pt;

public enum Elements implements CharElements {

    LEFT_UP('╝', pt(-1, 0), pt(0, 1)),
    UP_RIGHT('╚', pt(0, 1), pt(1, 0)),
    RIGHT_DOWN('╔', pt(1, 0), pt(0, -1)),
    DOWN_LEFT('╗', pt(0, -1), pt(-1, 0)),
    LEFT_RIGHT('═', pt(-1, 0),  pt(1, 0)),
    UP_DOWN('║', pt(0, 1),  pt(0, -1)),
    CROSS('╬', null, null),
    EMPTY(' ', null, null);

    private final char ch;
    private final Point from;
    private final Point to;

    Elements(char ch, Point from, Point to) {
        this.ch = ch;
        this.from = from;
        this.to = to;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public static Elements random(Dice dice) {
        return Elements.values()[dice.next(Elements.values().length - 1)];
    }

    public Point from() {
        return from;
    }

    public Point to() {
        return to;
    }
}
