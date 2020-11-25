package com.codenjoy.dojo.sudoku.model.level;

public interface LevelBuilder {
    String getMask();

    String getBoard();

    void build();
}
