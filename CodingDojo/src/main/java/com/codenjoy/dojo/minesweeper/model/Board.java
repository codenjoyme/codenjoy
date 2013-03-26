package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.*;
import com.codenjoy.dojo.services.Game;

import java.util.List;

public interface Board extends Game {

    List<Cell> getFreeCells();

    List<Cell> getCells();

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

    boolean isMine(int x, int y);

    boolean walkAt(int x, int y);

    boolean isFlag(int x, int y);

    boolean isSapper(int x, int y);

    int minesNear(int x, int y);
}