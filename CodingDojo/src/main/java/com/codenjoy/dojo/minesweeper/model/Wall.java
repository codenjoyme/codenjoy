package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by Sanja on 27.10.2014.
 */
public class Wall extends PointImpl implements State<Elements, Object> {

    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(Point point) {
        super(point);
    }

    @Override
    public Elements state(Object player, Object... alsoAtPoint) {
        return Elements.BORDER;
    }
}
