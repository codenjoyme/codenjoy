package com.codenjoy.dojo.battlecity.model.levels;

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


import com.codenjoy.dojo.battlecity.model.items.Border;

import java.util.LinkedList;
import java.util.List;

public class DefaultBorders {

    private final List<Border> result;
    private int size;

    public DefaultBorders(int size) {
        this.size = size;
        result = new LinkedList<Border>();
        addHorizontal();
        addVertical();
    }

    public List<Border> get() {
        return result;
    }

    private void addHorizontal() {
        for (int colNumber = 0; colNumber < size; colNumber++) {
            result.add(new Border(0, colNumber));
            result.add(new Border(size - 1, colNumber));
        }
    }

    private void addVertical() {
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            result.add(new Border(rowNumber, 0));
            result.add(new Border(rowNumber, size - 1));
        }
    }
}
