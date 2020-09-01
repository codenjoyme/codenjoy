package com.codenjoy.dojo.battlecity.model.prizes;

import com.codenjoy.dojo.services.Point;

public class PrizeFactoryImpl implements PrizeFactory {
    @Override
    public Prize getImmortality(Point pt) {
        return new Immortality(pt);
    }

    @Override
    public Prize getBreakingWalls(Point pt) {
        return new BreakingWalls(pt);
    }

    @Override
    public Prize getWalkingWater(Point pt) {
        return new WalkingWater(pt);
    }
}
