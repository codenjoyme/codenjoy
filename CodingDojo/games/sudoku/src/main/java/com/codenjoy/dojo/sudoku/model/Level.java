package com.codenjoy.dojo.sudoku.model;

import java.util.List;

public interface Level {

    int getSize();

    List<Cell> getCells();

    List<Wall> getWalls();
}
