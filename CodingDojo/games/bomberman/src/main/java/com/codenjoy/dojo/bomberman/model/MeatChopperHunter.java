package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Point;

public class MeatChopperHunter extends MeatChopper {

    private Hero prey;

    public MeatChopperHunter(Point pt, Hero prey) {
        super(pt, prey.field(), prey.getDice());
        this.prey = prey;
    }

    @Override
    public void tick() {
        // TODO закончить
    }
}
