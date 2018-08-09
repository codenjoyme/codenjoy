package com.codenjoy.dojo.minesweeper.model;

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

public enum Elements implements CharElements {

    BANG('Ѡ'),
    HERE_IS_BOMB('☻'),
    DETECTOR('☺'),
    FLAG('‼'),
    HIDDEN('*'),

    ONE_MINE('1'),
    TWO_MINES('2'),
    THREE_MINES('3'),
    FOUR_MINES('4'),
    FIVE_MINES('5'),
    SIX_MINES('6'),
    SEVEN_MINES('7'),
    EIGHT_MINES('8'),

    BORDER('☼'),
    NONE(' '),
    DESTROYED_BOMB('x');

    final char ch;

    Elements(char ch) {
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

    public static Elements printMinesCount(int minesNear) {
        switch (minesNear) {
            case 1 : return Elements.ONE_MINE;
            case 2 : return Elements.TWO_MINES;
            case 3 : return Elements.THREE_MINES;
            case 4 : return Elements.FOUR_MINES;
            case 5 : return Elements.FIVE_MINES;
            case 6 : return Elements.SIX_MINES;
            case 7 : return Elements.SEVEN_MINES;
            case 8 : return Elements.EIGHT_MINES;
            default : return Elements.NONE;
        }
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }
}
