package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class BulletPack extends PointImpl implements State<Elements, Player>{

    public BulletPack(int x, int y) {
        super(x, y);
    }

    public BulletPack(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BULLET_PACK;
    }
}
