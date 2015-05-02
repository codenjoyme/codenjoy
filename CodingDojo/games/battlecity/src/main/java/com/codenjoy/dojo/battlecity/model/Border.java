package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Border extends PointImpl implements State<Elements, Player> {
    public Border(int x, int y) {
        super(x, y);
    }

    public Border(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BATTLE_WALL;
    }
}
