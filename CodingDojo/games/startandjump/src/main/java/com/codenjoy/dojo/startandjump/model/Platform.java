package com.codenjoy.dojo.startandjump.model;

import com.codenjoy.dojo.services.*;

public class Platform extends PointImpl implements Tickable, State<Elements, Player> {

    private Direction direction;

    public Platform(int x, int y) {
        super(x, y);
    }

    public Platform(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.PLATFORM;
    }

    @Override
    public void tick() {
        move(x-1, y);
    }
}
