package com.codenjoy.dojo.excitebike.model.items.springboard;

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

public enum SpringboardElementType implements CharElements {

    SPRINGBOARD_LEFT_UP('╔'),
    SPRINGBOARD_LEFT('/'),
    SPRINGBOARD_LEFT_DOWN('╚'),
    SPRINGBOARD_TOP('═'),
    SPRINGBOARD_RIGHT_UP('╗'),
    SPRINGBOARD_RIGHT('\\'),
    SPRINGBOARD_RIGHT_DOWN('╝');

    final char ch;

    SpringboardElementType(char ch) {
        this.ch = ch;
    }

    public static SpringboardElementType valueOf(char ch) {
        for (SpringboardElementType el : SpringboardElementType.values()) {
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
