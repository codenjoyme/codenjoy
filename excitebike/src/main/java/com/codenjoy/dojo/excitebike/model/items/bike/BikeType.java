package com.codenjoy.dojo.excitebike.model.items.bike;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

public enum BikeType implements CharElements {
    BIKE('o'),
    BIKE_FALLEN('~'),
    BIKE_INCLINE_LEFT('('),
    BIKE_INCLINE_RIGHT(')'),
    OTHER_BIKE('e'),
    OTHER_BIKE_FALLEN('_'),
    OTHER_BIKE_INCLINE_LEFT('z'),
    OTHER_BIKE_INCLINE_RIGHT('x');

    final char ch;

    BikeType(char ch) {
        this.ch = ch;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static BikeType valueOf(char ch) {
        for (BikeType el : BikeType.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

}
