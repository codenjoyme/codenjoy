package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 24.12.13
 * Time: 3:41
 */
public class SudokuPrinter implements GamePrinter {

    private final Sudoku game;
    private Player player;

    private List<Cell> cells;
    private List<Point> walls;

    public SudokuPrinter(Sudoku game, Player player) {
        this.player = player;
        this.game = game;
    }

    @Override
    public boolean init() {
        cells = game.getCells();
        walls = game.getWalls();
        return true;
    }

    @Override
    public Elements get(Point pt) {
        if (cells.contains(pt)) {
            Cell cell = cells.get(cells.indexOf(pt));

            if (cell.isHidden()) {
                return Elements.NONE;
            } else {
                return Elements.valueOf(cell.getNumber());
            }
        }

        if (walls.contains(pt)) return Elements.BORDER;

        return Elements.NONE;
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать этот метод
    }
}
