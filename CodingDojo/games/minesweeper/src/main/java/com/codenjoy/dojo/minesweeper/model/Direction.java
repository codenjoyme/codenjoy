package com.codenjoy.dojo.minesweeper.model;

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


import com.codenjoy.dojo.services.PointImpl;

public enum Direction {

    UP(new PointImpl(0, 1)),
    DOWN(new PointImpl(0, -1)),
    LEFT(new PointImpl(-1, 0)),
    RIGHT(new PointImpl(1, 0)),
    UP_LEFT(new PointImpl(-1, 1)),
    UP_RIGHT(new PointImpl(1, 1)),
    DOWN_LEFT(new PointImpl(-1, -1)),
    DOWN_RIGHT(new PointImpl(1, -1));

    private PointImpl delta;

    Direction(PointImpl delta) {
        this.delta = delta;
    }

    public PointImpl change(PointImpl point) {
        point.change(delta);
        return point;
    }

}
