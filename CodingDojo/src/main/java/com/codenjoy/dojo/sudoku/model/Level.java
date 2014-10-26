package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

public interface Level {

    int getSize();

    List<Cell> getCells();

    List<Wall> getWalls();
}
