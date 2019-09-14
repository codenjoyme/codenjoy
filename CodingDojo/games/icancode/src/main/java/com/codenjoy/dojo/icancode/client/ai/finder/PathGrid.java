package com.codenjoy.dojo.icancode.client.ai.finder;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.icancode.client.Board;

/**
 * Created by Mikhail_Udalyi on 10.10.2016.
 */
public class PathGrid implements IPathGrid {

    private boolean[][] grid = null;

    private int width;
    private int height;

    public PathGrid(boolean[][] g) {
        this.grid = g;

        width = g.length;
        height = g[0].length;
    }

    public PathGrid(Board board) {
        this(board.size(), board.size());

        Point me = board.getMe();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board.isBarrierAt(i, j) && (me.getX() != i || me.getY() != j)) {
                    setGrid(i, j, true);
                }
            }
        }
    }

    public PathGrid(int width, int height) {
        this.width = width;
        this.height = height;
        grid = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = false;
            }
        }
    }

    public boolean getGrid(int x, int y) {
        if (x < 0 || y < 0) {
            return true; //true means it is a barrer.
        }

        if (x >= grid.length || y >= grid[0].length) {
            return true;
        }

        return grid[x][y];
    }

    public void setGrid(int x, int y, boolean yes) {
        grid[x][y] = yes;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public PathGrid copy() {
        PathGrid ret;
        boolean[][] g = new boolean[grid.length][grid[0].length];

        for (int i = 0; i < g.length; i++)
            for (int j = 0; j < g[0].length; j++)
                g[i][j] = grid[i][j];

        ret = new PathGrid(g);
        return ret;
    }

    public String toString() {
        String str = "";
        for (int j = 0; j < grid[0].length; j++) {
            for (int i = 0; i < grid.length; i++) {
                if (grid[i][j])
                    str += "#";
                else
                    str += "-";
            }
            str += "\n";
        }
        return str;
    }
}//PathGrid