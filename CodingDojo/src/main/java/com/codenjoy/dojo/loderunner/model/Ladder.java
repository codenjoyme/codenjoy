package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by Sanja on 23.10.2014.
 */
public class Ladder extends PointImpl implements State<Elements, Player> {

    public Ladder(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.LADDER;
    }
}
