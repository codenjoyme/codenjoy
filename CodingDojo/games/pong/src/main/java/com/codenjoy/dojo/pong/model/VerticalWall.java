package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by indigo on 01.08.2015.
 */
public class VerticalWall extends PointImpl implements State<Elements, Player> {

    public VerticalWall(Point pt) {
        super(pt);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.VERTICAL_WALL;
    }

}
