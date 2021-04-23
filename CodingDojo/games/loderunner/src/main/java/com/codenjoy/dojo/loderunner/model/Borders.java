package com.codenjoy.dojo.loderunner.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

public class Borders {

    private Border[][] borders;
    private List<Border> all;
    private int size;

    public Borders(int size) {
        this.size = size;
        all = new ArrayList<>(size);
        borders = new Border[size][size];
        for (int x = 0; x < size; x++) {
            borders[x] = new Border[size];
        }
    }

    public void addAll(List<Border> borders) {
        for (Border border : borders) {
            set(border, border);
            all.add(border);
        }
    }

    private void set(Point pt, Border border) {
        if (pt.isOutOf(size)) return;
        borders[pt.getX()][pt.getY()] = border;
    }

    public Border get(Point pt) {
        return borders[pt.getX()][pt.getY()];
    }

    public List<Border> all() {
        return all;
    }

    public boolean contains(Point pt) {
        return borders[pt.getX()][pt.getY()] != null;
    }
}
