package com.codenjoy.dojo.rubicscube.model;

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

public class Line {

    public static final int SIZE = 3;

    private Elements[] line = new Elements[SIZE];

    public Line(String line) {
        for (int index = 0; index < SIZE; index++) {
            this.line[index] = Elements.valueOf(line.charAt(index));
        }
    }

    public Line(Elements[] newLine) {
        this.line = newLine;
    }

    public Elements get(int index) {
        return line[index];
    }

    public Line invert() {
        Elements[] newLine = new Elements[SIZE];
        for (int index = 0; index < SIZE; index++) {
            newLine[SIZE - 1 - index] = line[index];
        }

        return new Line(newLine);
    }

    @Override
    public String toString() {
        String result = "";
        for (int index = 0; index < SIZE; index++) {
            result += get(index).value();
        }
        return result;
    }
}
