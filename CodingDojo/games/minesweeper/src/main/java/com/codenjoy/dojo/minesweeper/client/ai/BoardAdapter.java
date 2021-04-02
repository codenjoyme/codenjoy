package com.codenjoy.dojo.minesweeper.client.ai;

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


import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.minesweeper.client.ai.utils.BoardImpl;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public class BoardAdapter extends BoardImpl {
    private Board board;

    public BoardAdapter(Board board) {
        super("");
        this.board = board;
    }

    @Override
    public List<Point> get(Elements... elements) {
        return board.get(elements);
    }

    @Override
    public boolean isAt(int x, int y, Elements element) {
        return board.isAt(x, y, element);
    }

    @Override
    public Elements getAt(int x, int y) {
        return board.getAt(x, y);
    }

    @Override
    public int size() {
        if (board == null) {
            return 15;
        }
        return board.size();
    }

    @Override
    public String toString() {
        return board.toString();
    }

    @Override
    public List<Point> getWalls() {
        return board.get(Elements.BORDER);
    }

    @Override
    public boolean isAt(int x, int y, Elements... elements) {
        return board.isAt(x, y, elements);
    }

    @Override
    public Point getMe() {
        return board.getMe();
    }

    @Override
    public boolean isGameOver() {
        return board.isGameOver();
    }
}
