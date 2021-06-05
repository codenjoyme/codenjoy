package com.codenjoy.dojo.rubicscube.client;

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


import com.codenjoy.dojo.client.AbstractBoard;

public class Board extends AbstractBoard<Element> {

    private static final int LAYER1 = 0;

    @Override
    public Element valueOf(char ch) {
        return Element.valueOf(ch);
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        for (int y = 0; y < size - 3; y++) {
            for (int x = 0; x < size; x++) {
                result.append(field[LAYER1][x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }

}
