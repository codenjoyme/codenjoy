package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by indigo on 01.08.2015.
 */
public class HorizontalWall extends PointImpl implements State<Elements, Player> {

    public HorizontalWall(Point pt) {
        super(pt);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.HORIZONTAL_WALL;
    }
}
