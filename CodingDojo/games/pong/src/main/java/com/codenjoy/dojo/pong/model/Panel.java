package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Panel extends PointImpl implements State<Elements, Player>, Barrier {

    private Hero owner;

    @Override
    public BarrierOrientation getOrientation() {
        return BarrierOrientation.VERTICAL;
    }

    public Panel(Point pt, Hero owner) {
        super(pt);
        this.owner = owner;
    }

    public Panel(int x, int y, Hero owner) {
        super(x,y);
        this.owner = owner;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (owner == player.getHero()) {
            return Elements.HERO;
        } else {
            return Elements.PANEL;
        }
    }

}
