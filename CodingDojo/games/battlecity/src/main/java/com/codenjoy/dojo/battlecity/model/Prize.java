package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Prize extends PointImpl implements State<Elements, Player> {

    private Elements elements;

    public Prize(Point pt, Elements elements) {
        super(pt);
        this.elements = elements;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return elements;
    }
}
