package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

public class MinesweeperPrinter implements GamePrinter {
    private int size;
    private Field board;

    public MinesweeperPrinter(Field board) {
        this.board = board;
        size = board.getSize();
    }

    private boolean isBoardBound(int x, int y) {
        return y == 0 || x == 0 || y == size - 1 || x == size - 1;
    }

    @Override
    public boolean init() {
        return true;
    }

    @Override
    public char get(Point pt) {
        if (isBoardBound(pt.getX(), pt.getY())) {
            return Elements.BORDER.ch;
        }

        if (board.isSapper(pt)) {
            if (board.isSapperOnMine()) {
                return Elements.BANG.ch;
            } else {
                return Elements.DETECTOR.ch;
            }
        }

        if (board.isGameOver() && board.isMine(pt)) {
            if (board.isFlag(pt)) {
                return Elements.DESTROYED_BOMB.ch;
            } else {
                return Elements.HERE_IS_BOMB.ch;
            }
        }

        if (board.isFlag(pt)) {
            return Elements.FLAG.ch;
        }

        if (board.walkAt(pt) || board.isGameOver()) {
            int minesNear = board.minesNear(pt);
            if (minesNear == 0) {
                return Elements.NO_MINE.ch;
            } else {
                return Elements.printMinesCount(minesNear).ch;
            }
        }

        return Elements.HIDDEN.ch;
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать его
    }
}
