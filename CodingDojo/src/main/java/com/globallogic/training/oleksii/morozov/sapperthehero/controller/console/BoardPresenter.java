package com.globallogic.training.oleksii.morozov.sapperthehero.controller.console;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.Board;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.CellImpl;

class BoardPresenter {
    private static final char SAPPER_CHAR = '@';
    private static final char MINE_CHAR = '*';
    private static final char FREE_CELL_CHAR = '.';
    private static final char BOUND_BOARD_CHAR = '#';
    private static final String MESSAGE_MINES_ON_BOARD = "mines on board: ";
    private static final String MESSAGE_MINES_NEAR_ME = "mines near supper: ";
    private static final String MESSAGE_MY_DETECTOR_CHARGE = "mine detector charge: ";
    private int boardSize;
    private boolean showMines;
    private Board board;

    BoardPresenter(Board board) {
        this(false, board);
    }

    BoardPresenter(boolean showMines, Board board) {
        this.showMines = showMines;
        this.board = board;
        boardSize = board.getSize();
    }

    String print() {
        StringBuffer result = new StringBuffer();
        for (int y = -1; y <= boardSize; y++) {
            for (int x = -1; x <= boardSize; x++) {
                if (isBoardBound(y, x)) {
                    result.append(printCellObject(BOUND_BOARD_CHAR));
                } else if (isSapper(y, x)) {
                    result.append(printCellObject(SAPPER_CHAR));
                } else if (showMines && isMine(y, x)) {
                    result.append(printCellObject(MINE_CHAR));
                } else {
                    result.append(printCellObject(FREE_CELL_CHAR));
                }
            }
            result.append("\n");
        }
        result.append(printDetails());
        return result.toString();
    }

    private String printDetails() {
        return MESSAGE_MINES_ON_BOARD + board.getMinesCount() + "\n"
                + MESSAGE_MINES_NEAR_ME + board.getMinesNearSapper() + "\n"
                + MESSAGE_MY_DETECTOR_CHARGE + board.getSapper().getMineDetectorCharge();
    }

    private boolean isBoardBound(int y, int x) {
        return y == -1 || x == -1 || y == boardSize || x == boardSize;
    }

    private boolean isSapper(int y, int x) {
        return new CellImpl(x, y).equals(board.getSapper());
    }

    private boolean isMine(int y, int x) {
        return board.getMines().contains(new CellImpl(x, y));
    }

    private String printCellObject(char cellObject) {
        return cellObject + " ";
    }

}
