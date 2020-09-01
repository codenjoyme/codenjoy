package com.codenjoy.dojo.battlecity.model.prizes;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public abstract class Prize extends PointImpl implements State<Elements, Player> {
    private Point pt;

    public Prize(Point pt) {
        super(pt);
        this.pt = pt;
    }

    public Point getPoint() {
        return pt;
    }
}
