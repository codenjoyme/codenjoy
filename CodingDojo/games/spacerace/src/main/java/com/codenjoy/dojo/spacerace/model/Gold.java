package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт Золото на поле
 */
public class Gold extends PointImpl implements State<Elements, Player> {

    public Gold(int x, int y) {
        super(x, y);
    }

    public Gold(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.GOLD;
    }
}
