package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by Sanja on 26.10.2014.
 */
public class Gold extends PointImpl implements State<Elements, Player> {

    public Gold(Point point) {
        super(point);
    }

    public Gold(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.GOLD;
    }
}