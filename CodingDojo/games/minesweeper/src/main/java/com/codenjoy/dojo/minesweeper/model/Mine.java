package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:44 PM
 */
public class Mine extends PointImpl implements State<Elements, Object> {

    private Field board;

    public Mine(Point point) {
        super(point);
    }

    public Mine(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        if (!board.isGameOver()) return null;

        if (board.isFlag(this)) {
            return Elements.DESTROYED_BOMB;
        } else {
            return Elements.HERE_IS_BOMB;
        }
    }

    public void setBoard(Field board) {
        this.board = board;
    }
}
