package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Wall extends PointImpl implements State<Elements, Player>, Barrier {

    BarrierOrientation orientation;

    public Wall(Point pt, BarrierOrientation orientation) {
        super(pt);
        this.orientation = orientation;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        switch (orientation) {
            case VERTICAL:
                return Elements.VERTICAL_WALL;
            case HORISONTAL:
                return Elements.HORIZONTAL_WALL;
            default:
                return Elements.HORIZONTAL_WALL;
        }
    }

    @Override
    public BarrierOrientation getOrientation() {
        return orientation;
    }
}
