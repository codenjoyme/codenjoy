package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Flag extends PointImpl implements State<Elements, Object> {

    public Flag(Point point) {
        super(point);
    }

    public Flag(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        return Elements.FLAG;
    }
}