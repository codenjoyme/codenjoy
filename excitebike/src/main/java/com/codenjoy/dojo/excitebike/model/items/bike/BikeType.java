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
    BIKE('B'),
    BIKE_AT_ACCELERATOR('A'),
    BIKE_AT_INHIBITOR('I'),
    BIKE_AT_LINE_CHANGER_UP('U'),
    BIKE_AT_LINE_CHANGER_DOWN('D'),
    BIKE_AT_DOWNED_BIKE('k'),

    BIKE_FALLEN('b'),
    BIKE_FALLEN_AT_ACCELERATOR('a'),
    BIKE_FALLEN_AT_INHIBITOR('i'),
    BIKE_FALLEN_AT_LINE_CHANGER_UP('u'),
    BIKE_FALLEN_AT_LINE_CHANGER_DOWN('d'),
    BIKE_FALLEN_AT_BORDER('c'),
    BIKE_FALLEN_AT_OBSTACLE('o'),

    BIKE_INCLINE_LEFT('L'),
    BIKE_INCLINE_LEFT_AT_ACCELERATOR('K'),
    BIKE_INCLINE_LEFT_AT_INHIBITOR('G'),
    BIKE_INCLINE_LEFT_AT_LINE_CHANGER_UP('H'),
    BIKE_INCLINE_LEFT_AT_LINE_CHANGER_DOWN('J'),

    BIKE_INCLINE_RIGHT('R'),
    BIKE_INCLINE_RIGHT_AT_ACCELERATOR('C'),
    BIKE_INCLINE_RIGHT_AT_INHIBITOR('M'),
    BIKE_INCLINE_RIGHT_AT_LINE_CHANGER_UP('N'),
    BIKE_INCLINE_RIGHT_AT_LINE_CHANGER_DOWN('O'),

    OTHER_BIKE('E'),
    OTHER_BIKE_AT_ACCELERATOR('P'),
    OTHER_BIKE_AT_INHIBITOR('Q'),
    OTHER_BIKE_AT_LINE_CHANGER_UP('S'),
    OTHER_BIKE_AT_LINE_CHANGER_DOWN('T'),
    OTHER_BIKE_AT_DOWNED_BIKE('l'),

    OTHER_BIKE_FALLEN('e'),
    OTHER_BIKE_FALLEN_AT_ACCELERATOR('p'),
    OTHER_BIKE_FALLEN_AT_INHIBITOR('q'),
    OTHER_BIKE_FALLEN_AT_LINE_CHANGER_UP('s'),
    OTHER_BIKE_FALLEN_AT_LINE_CHANGER_DOWN('t'),
    OTHER_BIKE_FALLEN_AT_BORDER('r'),
    OTHER_BIKE_FALLEN_AT_OBSTACLE('v'),

    OTHER_BIKE_INCLINE_LEFT('Z'),
    OTHER_BIKE_INCLINE_LEFT_AT_ACCELERATOR('Y'),
    OTHER_BIKE_INCLINE_LEFT_AT_INHIBITOR('X'),
    OTHER_BIKE_INCLINE_LEFT_AT_LINE_CHANGER_UP('W'),
    OTHER_BIKE_INCLINE_LEFT_AT_LINE_CHANGER_DOWN('V'),

    OTHER_BIKE_INCLINE_RIGHT('F'),
    OTHER_BIKE_INCLINE_RIGHT_AT_ACCELERATOR('f'),
    OTHER_BIKE_INCLINE_RIGHT_AT_INHIBITOR('g'),
    OTHER_BIKE_INCLINE_RIGHT_AT_LINE_CHANGER_UP('h'),
    OTHER_BIKE_INCLINE_RIGHT_AT_LINE_CHANGER_DOWN('j');

    final char ch;

    BikeType(char ch) {
        this.ch = ch;
    }

    public static BikeType valueOf(char ch) {
        for (BikeType el : BikeType.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

}
