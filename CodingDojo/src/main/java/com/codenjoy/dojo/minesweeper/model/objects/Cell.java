package com.codenjoy.dojo.minesweeper.model.objects;

public interface Cell {

    int getX();

    int getY();

    void changeTo(Cell cell);

}