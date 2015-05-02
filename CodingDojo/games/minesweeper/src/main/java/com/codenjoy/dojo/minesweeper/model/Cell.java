package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Cell extends PointImpl implements State<Elements, Object> {

    private Field board;

    public Cell(Point point) {
        super(point);
    }

    public Cell(int x, int y, Field board) {
        super(x, y);
        this.board = board;
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (board.walkAt(this) || board.isGameOver()) {
            int minesNear = board.minesNear(this);
            if (minesNear == 0) {
                return Elements.NONE;
            } else {
                return Elements.printMinesCount(minesNear);
            }
        }

        return Elements.HIDDEN;
    }
}
