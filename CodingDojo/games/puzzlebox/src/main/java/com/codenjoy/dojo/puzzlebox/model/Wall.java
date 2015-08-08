package com.codenjoy.dojo.puzzlebox.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by indigo on 01.08.2015.
 */
public class Wall extends PointImpl implements State<Elements, Player> {

    public Wall(Point pt) {
        super(pt);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.WALL;
    }
}
