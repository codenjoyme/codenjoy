package com.codenjoy.dojo.hex.model;

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
import java.util.LinkedList;
import java.util.List;

public enum Elements implements CharElements {

    NONE(' '),
    WALL('☼'),
    MY_HERO('☺'),
    HERO1('☻'),
    HERO2('♥'),
    HERO3('♦'),
    HERO4('♣'),
    HERO5('♠'),
    HERO6('•'),
    HERO7('◘'),
    HERO8('○'),
    HERO9('◙'),
    HERO10('♂'),
    HERO11('♀');

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

    public static List<Elements> heroesElements() {
        return new LinkedList<Elements>(Arrays.asList(Elements.values())) {{
            remove(Elements.NONE);
            remove(Elements.WALL);
            remove(Elements.MY_HERO);
        }};
    }

    public static Elements valueOf(char ch) {
        return Arrays.stream(Elements.values())
                .filter(el -> el.ch == ch)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such element for " + ch));
    }

}
