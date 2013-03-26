package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.CellImpl;
import com.codenjoy.dojo.services.Printer;

public class MinesweeperPrinter implements Printer {
    private static final String MESSAGE_MINES_ON_BOARD = "mines on board: ";
    private static final String MESSAGE_MINES_NEAR_ME = "mines near sapper: ";
    private static final String MESSAGE_MY_DETECTOR_CHARGE = "mine detector charge: ";
    private int boardSize;
    private Board board;

    public MinesweeperPrinter(Board board) {
        this.board = board;
        boardSize = board.getSize();
    }

    @Override
    public String print() {
        StringBuffer result = new StringBuffer();
        for (int y = boardSize; y >= -1; y--) {
            for (int x = -1; x <= boardSize; x++) {
                result.append(printCell(x, y));
            }
            result.append("\n");
        }
        return result.toString();
    }

    private PlotColor printCell(int x, int y) {
        if (isBoardBound(x, y)) {
            return PlotColor.BORDER;
        } else if (board.isSapper(x, y)) {
            if (board.isSapperOnMine()) {
                return PlotColor.BANG;
            } else {
                int minesNear = board.getMinesNearSapper();
                return PlotColor.printMinesCount(minesNear);
            }
        } else if (board.isFlag(x, y)) {
            return PlotColor.FLAG;
        } else if (board.isGameOver() && board.isMine(x, y)) {
            return PlotColor.HERE_IS_BOMB;
        } else if (board.walkAt(x, y)) {
            int minesNear = board.minesNear(x, y);
            if (minesNear == 0) {
                return PlotColor.NO_MINE;
            } else {
                return PlotColor.printMinesCount(minesNear);
            }
        } else {
            return PlotColor.HIDDEN;
        }
    }

    private boolean isBoardBound(int x, int y) {
        return y == -1 || x == -1 || y == boardSize || x == boardSize;
    }
}
