package com.codenjoy.dojo.battlecity.model.prizes;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.Point;

public class WalkingWater extends Prize {

    public WalkingWater(Point pt) {
        super(pt);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.PRIZE_WALKING_ON_WATER;
    }
}
