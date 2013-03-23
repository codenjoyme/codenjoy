package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.CellImpl;
import com.codenjoy.dojo.services.Printer;

public class MinesweeperPrinter implements Printer {
    private static final String MESSAGE_MINES_ON_BOARD = "mines on board: ";
    private static final String MESSAGE_MINES_NEAR_ME = "mines near sapper: ";
    private static final String MESSAGE_MY_DETECTOR_CHARGE = "mine detector charge: ";
    private int boardSize;
    private boolean showMines;
    private Board board;

    public MinesweeperPrinter(Board board) {
        this(false, board);
    }

    public MinesweeperPrinter(boolean showMines, Board board) {
        this.showMines = showMines;
        this.board = board;
        boardSize = board.getSize();
    }

    @Override
    public String print() {
        StringBuffer result = new StringBuffer();
        for (int y = -1; y <= boardSize; y++) {
            for (int x = -1; x <= boardSize; x++) {
                if (isBoardBound(y, x)) {
                    result.append(PlotColor.BORDER                                                                                                                                    );
                } else if (isSapper(y, x)) {
                    result.append(PlotColor.DETECTOR);
                } else if (showMines && isMine(y, x)) {
                    result.append(PlotColor.HERE_IS_BOMB);
                } else {
                    result.append(PlotColor.HIDDEN);
                }
            }
            result.append("\n");
        }
        //result.append(printDetails());
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

}
