package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Wall extends PointImpl implements State<Elements, Player> {

    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(Point xy) {
        super(xy);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.WALL;
    }
}
