package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 24.12.13
 * Time: 3:41
 */
public class RubicsCubePrinter implements GamePrinter {

    private final RubicsCube game;

    private List<Cell> cells;

    public RubicsCubePrinter(RubicsCube game) {
        this.game = game;
    }

    @Override
    public boolean init() {
        cells = game.getCells();
        return true;
    }

    @Override
    public Element get(Point pt) {
        if (!cells.contains(pt)) {
            return Element.NONE;
        }

        Cell cell = cells.get(cells.indexOf(pt)); // TODO как это неоптимально!
        return Element.valueOf(cell.getColor());
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать этот метод
    }
}
