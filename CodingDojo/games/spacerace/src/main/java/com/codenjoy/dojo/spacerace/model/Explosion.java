package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by Pyatnitskiy.a on 01.08.2015.
 */
public class Explosion extends PointImpl implements State<Elements, Player> {

    public Explosion(Point pt) {
        super(pt);
    }

    public Explosion(int x, int y) {
        super(x, y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.EXPLOSION;
    }
}
