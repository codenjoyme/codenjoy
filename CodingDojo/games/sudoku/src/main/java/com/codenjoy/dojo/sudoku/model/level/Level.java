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
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.sudoku.model.Cell;
import com.codenjoy.dojo.sudoku.model.Wall;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.List;

import static com.codenjoy.dojo.games.sudoku.Element.*;
import static java.util.stream.Collectors.toList;

public class Level extends AbstractLevel {

    private static final int SIZE = 9;

    private String all;
    private String mask;

    public Level(String all) {
        super("");
        this.all = all;

        split(LevelUtils.clear(all));
        parse();
    }

    public Level(String map, String mask) {
        super(map);
        this.mask = mask;

        parse();
    }

    private void parse() {
        map = withBorders(map);
        mask = withBorders(mask);

        if (map.length() != mask.length()) {
            throw new IllegalArgumentException("Маска не совпадает с полем по размеру: " +
                    map.length() + "-" + mask.length());
        }

        resize();
    }

    private String withBorders(String input) {
        StringBuffer result = new StringBuffer();

        result.append("☼☼☼☼☼☼☼☼☼☼☼☼☼");
        for (int i = 0; i <= SIZE - 1; i++) {
            result.append('☼');
            String line = input.substring(i * SIZE, (i + 1) * SIZE);
            result.append(line, 0, 3).append('☼');
            result.append(line, 3, 6).append('☼');
            result.append(line, 6, 9).append('☼');
            if ((i + 1) % 3 == 0) {
                result.append("☼☼☼☼☼☼☼☼☼☼☼☼☼");
            }
        }
        return result.toString();
    }

    private void split(String all) {
        StringBuilder mask = new StringBuilder();
        StringBuilder board = new StringBuilder();

        for (int index = 0; index < all.length(); index++) {
            char ch = all.charAt(index);
            if (index % 2 == 0) {
                board.append(ch);
            } else {
                mask.append(ch);
            }
        }

        this.mask = mask.toString();
        this.map = board.toString();
    }

    public List<Cell> cells() {
        return find((pt, el) -> {
                    int i = xy.getLength(pt.getX(), pt.getY());
                    boolean visible = mask.charAt(i) != HIDDEN.ch();
                    return new Cell(pt, el.value(), visible);
                },
                valuesExcept(BORDER, NONE, HIDDEN))
                .stream()
                .sorted()
                .collect(toList());
    }

    public List<Wall> walls() {
        return find(Wall::new, BORDER);
    }

    public String mask() {
        return mask;
    }

    public String map() {
        return map;
    }

    public String all() {
        return all;
    }
}
