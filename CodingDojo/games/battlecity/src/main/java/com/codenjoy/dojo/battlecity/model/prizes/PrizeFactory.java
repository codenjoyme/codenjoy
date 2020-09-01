package com.codenjoy.dojo.battlecity.model.prizes;

import com.codenjoy.dojo.services.Point;

public interface PrizeFactory {
    Prize getImmortality(Point pt);

    Prize getBreakingWalls(Point pt);

    Prize getWalkingWater(Point pt);
}
