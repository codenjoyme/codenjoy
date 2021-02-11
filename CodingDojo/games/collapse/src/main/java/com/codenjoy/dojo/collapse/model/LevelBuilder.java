package com.codenjoy.dojo.collapse.model;

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


import com.codenjoy.dojo.services.Dice;

import java.util.ArrayList;

public class LevelBuilder {

    private int[][] field;
    private int size;
    private Dice dice;

    public LevelBuilder(Dice dice, int size) {
        this.dice = dice;
        this.size = size;
        field = new int[size][size];

        for (int x = 1; x < size - 1; x++) {
            for (int y = 1; y < size - 1; y++) {
                Container<Integer, Integer> numbers = new Container<Integer, Integer>();
                for (int i = 1; i < 9; i++) {
                    numbers.add(i);
                }

                for (int dx = -1; dx <= 1; dx ++) {
                    for (int dy = -1; dy <= 1; dy ++) {
                        if (dx == 0 && dy == 0) continue;

                        numbers.remove(field[x + dx][y + dy]);
                    }
                }

                int index = dice.next(numbers.size());
                field[x][y] = new ArrayList<>(numbers.values()).get(index);
            }
        }
    }

    public String getBoard() {
        StringBuffer result = new StringBuffer();

        char borderChar = Elements.BORDER.ch();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x == 0 || y == 0 || x == size - 1 || y == size - 1) {
                    result.append(borderChar);
                } else {
                    result.append(field[x][y]);
                }
            }
        }
        return result.toString();
    }

}
