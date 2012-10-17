package com.globallogic.sapperthehero.controller;

import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Cell;

public class BoardPrint {
    private static final char sapperChar = '@';
    private static final char mineChar = '*';
    private static final char freeCellChar = '.';
    private static final char boundBoardChar = '#';
    private static final String МИН_НА_ПОЛЕ = "мин на поле: ";
    private static final String МИН_РЯДОМ_СО_МНОЙ = "мин рядом со мной: ";
    private static final String ЗАРЯОВ_У_ДЕТЕКТОРА = "заряов у детектора: ";

    private boolean cheats;

    public BoardPrint(boolean cheats) {
        this.cheats = cheats;
    }

    public void printBoard(Board board) {
        int boardSize = board.getBoardSize();
        for (int columnIndex = -1; columnIndex <= boardSize; columnIndex++) {
            for (int rowIndex = -1; rowIndex <= boardSize; rowIndex++) {
                if (columnIndex == -1 || rowIndex == -1 || columnIndex == boardSize || rowIndex == boardSize) {
                    printCellObject(boundBoardChar);
                } else if (new Cell(rowIndex, columnIndex).equals(board.getSapper())) {
                    printCellObject(sapperChar);
                } else if (cheats && board.getMines().contains(new Cell(rowIndex, columnIndex))) {
                    printCellObject(mineChar);
                } else {
                    printCellObject(freeCellChar);
                }
            }
            System.out.println();
        }
        System.out.println(МИН_НА_ПОЛЕ + board.getMinesCount());
        System.out.println(МИН_РЯДОМ_СО_МНОЙ + board.getMinesNearSapper());
        System.out.println(ЗАРЯОВ_У_ДЕТЕКТОРА + board.getSapper().getMineDetectorCharge());
    }

    private void printCellObject(char cellObject) {
        System.out.print(cellObject + " ");
    }

}
