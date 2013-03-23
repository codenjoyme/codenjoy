package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.*;

import java.util.List;

public interface Board {

    List<CellImpl> getFreeCells();

    List<CellImpl> getCells();

    int getSize();

    Sapper getSapper();

    List<Mine> getMines();

    int getMinesCount();

    void sapperMoveTo(Direction direction);

    boolean isSapperOnMine();

    Cell getCellPossiblePosition(Direction direction);

    int getMinesNearSapper();

    boolean isEmptyDetectorButPresentMines();

    boolean isWin();

    void useMineDetectorToGivenDirection(Direction direction);

    Mine createMineOnPositionIfPossible(Cell cell);

    int getTurn();

    boolean isGameOver();
}