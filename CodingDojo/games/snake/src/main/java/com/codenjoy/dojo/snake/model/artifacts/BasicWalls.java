package com.codenjoy.dojo.snake.model.artifacts;

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


import com.codenjoy.dojo.snake.model.Walls;

public class BasicWalls extends Walls implements Iterable<Wall> {
    public BasicWalls(int boardSize) {
        super();

        for (int x = 0; x < boardSize; x++) {
            add(x, 0);
            add(x, boardSize - 1);
        }

        final int D = 1; // учитывать уже существующие вертикальные стены
        for (int y = D; y < boardSize - D; y++) {
            add(0, y);
            add(boardSize - 1, y);
        }
    }


}
