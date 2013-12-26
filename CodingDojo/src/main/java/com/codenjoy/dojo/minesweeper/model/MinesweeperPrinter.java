package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.GamePrinter;

public class MinesweeperPrinter implements GamePrinter {
    private int boardSize;
    private Board board;

    public MinesweeperPrinter(Board board) {
        this.board = board;
        boardSize = board.getSize();
    }

    private boolean isBoardBound(int x, int y) {
        return y == 0 || x == 0 || y == boardSize - 1 || x == boardSize - 1;
    }

    @Override
    public Enum get(int x, int y) {
        if (isBoardBound(x, y)) {
            return Elements.BORDER;
        }

        if (board.isSapper(x, y)) {
            if (board.isSapperOnMine()) {
                return Elements.BANG;
            } else {
                return Elements.DETECTOR;
            }
        }

        if (board.isGameOver() && board.isMine(x, y)) {
            if (board.isFlag(x, y)) {
                return Elements.DESTROYED_BOMB;
            } else {
                return Elements.HERE_IS_BOMB;
            }
        }

        if (board.isFlag(x, y)) {
            return Elements.FLAG;
        }

        if (board.walkAt(x, y) || board.isGameOver()) {
            int minesNear = board.minesNear(x, y);
            if (minesNear == 0) {
                return Elements.NO_MINE;
            } else {
                return Elements.printMinesCount(minesNear);
            }
        }

        return Elements.HIDDEN;
    }
}
