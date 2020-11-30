package com.codenjoy.dojo.sudoku.model;

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

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Elements implements CharElements {

    NONE(' '),   // отгадай, что тут за цифра
    BORDER('☼'), // граница, проигнорь ее ;) она не учитывается в координатах
    HIDDEN('*'), // если число не отображается на поле
    ONE('1'),    // циферки
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9');

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

    public static Elements valueOf(int n) {
        for (Elements el : Elements.values()) {
            if (String.valueOf(n).equals("" + el.ch)) {
                return el;
            }
        }
        throw new IllegalArgumentException("Нет такого елемента: " + n);
    }

    public static Elements valueOf(char ch) {
        for (Elements el : Elements.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }

    public static Elements[] valuesExcept(Elements... excluded) {
        List<Elements> list = Arrays.asList(excluded);
        return Arrays.stream(values())
                .filter(el -> !list.contains(el))
                .collect(toList())
                .toArray(new Elements[0]);
    }

    public Integer value() {
        if (this == NONE) {
            return 0;
        }
        if (this == BORDER || this == HIDDEN) {
            return -1;
        }
        return Integer.valueOf("" + ch);
    }
}
