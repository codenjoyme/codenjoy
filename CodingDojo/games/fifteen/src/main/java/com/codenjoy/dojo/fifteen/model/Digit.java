package com.codenjoy.dojo.fifteen.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Created by Administrator on 01.08.2015.
 */
public class Digit extends PointImpl implements State<Elements, Player> {

    private Elements element;

    public Digit(Point xy, Elements element) {
        super(xy);
        this.element = element;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return element;
    }
}
