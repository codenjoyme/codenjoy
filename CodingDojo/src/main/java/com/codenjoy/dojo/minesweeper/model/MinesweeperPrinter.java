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
                if (isBoardBound(x, y)) {
                    result.append(PlotColor.BORDER                                                                                                                                    );
                } else if (board.isFlag(x, y)) {
                    result.append(PlotColor.FLAG);
                } else if (board.isSapper(x, y)) {
                    if (board.isSapperOnMine()) {
                        result.append(PlotColor.BANG);
                    } else {
                        switch (board.getMinesNearSapper()) {
                            case 0 : result.append(PlotColor.DETECTOR); break;
                            case 1 : result.append(PlotColor.ONE_MINE); break;
                            case 2 : result.append(PlotColor.TWO_MINES); break;
                            case 3 : result.append(PlotColor.THREE_MINES); break;
                            case 4 : result.append(PlotColor.FOUR_MINES); break;
                            case 5 : result.append(PlotColor.FIVE_MINES); break;
                            case 6 : result.append(PlotColor.SIX_MINES); break;
                            case 7 : result.append(PlotColor.SEVEN_MINES); break;
                            case 8 : result.append(PlotColor.EIGHT_MINES); break;
                        }
                    }
                } else if (board.isGameOver() && board.isMine(x, y)) {
                    result.append(PlotColor.HERE_IS_BOMB);
                } else if (board.walkAt(x, y)) {
                    result.append(PlotColor.NO_MINE);
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

    private boolean isBoardBound(int x, int y) {
        return y == -1 || x == -1 || y == boardSize || x == boardSize;
    }
}
