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

    BANG('Ѡ', -7),
    HERE_IS_BOMB('☻', -6),
    DETECTOR('☺', -5),
    FLAG('‼', -4),
    HIDDEN('*', -3),
    BORDER('☼', -2),
    DESTROYED_BOMB('x', -1),

    NONE(' ', 0),
    ONE_MINE('1', 1),
    TWO_MINES('2', 2),
    THREE_MINES('3', 3),
    FOUR_MINES('4', 4),
    FIVE_MINES('5', 5),
    SIX_MINES('6', 6),
    SEVEN_MINES('7', 7),
    EIGHT_MINES('8', 8);

    final char ch;
    final int value;

    Elements(char ch, int value) {
        this.ch = ch;
        this.value = value;
    }

    @Override
    public char ch() {
        return ch;
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public static Elements get(int value) {
        for (Elements element : values()) {
            if (element.value == value) {
                return element;
            }
        }
        throw new IllegalArgumentException("Value not found for: " + value);
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
