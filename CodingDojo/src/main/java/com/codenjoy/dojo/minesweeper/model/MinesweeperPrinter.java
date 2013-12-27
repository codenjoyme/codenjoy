package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

public class MinesweeperPrinter implements GamePrinter {
    private int size;
    private Board board;

    public MinesweeperPrinter(Board board) {
        this.board = board;
        size = board.getSize();
    }

    private boolean isBoardBound(int x, int y) {
        return y == 0 || x == 0 || y == size - 1 || x == size - 1;
    }

    @Override
    public void init() {
        // do nothing
    }

    @Override
    public Enum get(Point pt) {
        if (isBoardBound(pt.getX(), pt.getY())) {
            return Elements.BORDER;
        }

        if (board.isSapper(pt)) {
            if (board.isSapperOnMine()) {
                return Elements.BANG;
            } else {
                return Elements.DETECTOR;
            }
        }

        if (board.isGameOver() && board.isMine(pt)) {
            if (board.isFlag(pt)) {
                return Elements.DESTROYED_BOMB;
            } else {
                return Elements.HERE_IS_BOMB;
            }
        }

        if (board.isFlag(pt)) {
            return Elements.FLAG;
        }

        if (board.walkAt(pt) || board.isGameOver()) {
            int minesNear = board.minesNear(pt);
            if (minesNear == 0) {
                return Elements.NO_MINE;
            } else {
                return Elements.printMinesCount(minesNear);
            }
        }

        return Elements.HIDDEN;
    }
}
