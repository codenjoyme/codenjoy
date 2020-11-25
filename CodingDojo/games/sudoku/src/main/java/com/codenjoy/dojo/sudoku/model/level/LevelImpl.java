package com.codenjoy.dojo.sudoku.model.level;

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


import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.sudoku.model.Cell;
import com.codenjoy.dojo.sudoku.model.Elements;
import com.codenjoy.dojo.sudoku.model.Wall;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;
    private String mask;

    public LevelImpl(String map, String mask) {
        this.map = map;
        this.mask = mask;

        if (map.length() != mask.length()) {
            throw new IllegalArgumentException("Маска не совпадает с полем по размеру: " +
                    map.length() + "-" + mask.length());
        }

        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Cell> getCells() {
        List<Cell> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            char ch = map.charAt(index);
            char mch = mask.charAt(index);
            if (ch != Elements.BORDER.ch()) {
                result.add(new Cell(xy.getXY(index), Integer.parseInt("" + ch), mch != '?'));
            }
        }

        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<Wall>();
        for (int index = 0; index < map.length(); index++) {
            char ch = map.charAt(index);
            if (ch == Elements.BORDER.ch()) {
                result.add(new Wall(xy.getXY(index)));
            }
        }
        return result;
    }
}
