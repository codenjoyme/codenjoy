package com.codenjoy.dojo.tetris.client;

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


import com.codenjoy.dojo.services.printer.CharElements;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public enum Element implements CharElements {

    BLUE('I', 2),
    CYAN('J', 3),
    ORANGE('L', 4),
    YELLOW('O', 1),
    GREEN('S', 5),
    PURPLE('T', 7),
    RED('Z', 6),
    NONE('.', 0);

    final char ch;
    final int index;

    Element(char ch, int index) {
        this.ch = ch;
        this.index = index;
    }

    @Override
    public char ch() {
        return ch;
    }

    @Override
    public String toString() {
        return String.valueOf(ch);
    }

    public int index() {
        return index;
    }

    public static Element valueOf(char ch) {
        for (Element el : Element.values()) {
            if (el.ch == ch) {
                return el;
            }
        }
        throw new IllegalArgumentException("No such element for " + ch);
    }


    public static Element[] valuesExcept(Element... excluded) {
        List<Element> list = Arrays.asList(excluded);
        return Arrays.stream(values())
                .filter(el -> !list.contains(el))
                .collect(toList())
                .toArray(new Element[0]);
    }
}
