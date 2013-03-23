package com.codenjoy.dojo.minesweeper.model.objects;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:44 PM
 */
public class Mine extends CellImpl {

    public Mine(Cell cell) {
        super(cell);
    }

    public Mine(int x, int y) {
        super(x, y);
    }
}
