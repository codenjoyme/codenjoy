package com.codenjoy.dojo.a2048.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Number extends PointImpl implements State<Elements, Player> {

    private int number;

    public Number(int number, Point pt) {
        super(pt);
        this.number = number;
    }

    public Number(int number, int x, int y) {
        super(x, y);
        this.number = number;
    }

    public int get() {
        return number;
    }

    public int next() {
        return number*2;
    }

    @Override
    public String toString() {
        return String.format("{%s=%s}", super.toString(), number);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (number == Numbers.BREAK) {
            return Elements._x;
        }

        if (number == Numbers.NONE) {
            return Elements.NONE;
        }
        return Elements.valueOf(number);
    }
}
