package com.codenjoy.dojo.minesweeper.model.objects;

public interface Cell {

    int getX();

    int getY();

    Cell moveTo(Direction direction);

    Cell clone();

}