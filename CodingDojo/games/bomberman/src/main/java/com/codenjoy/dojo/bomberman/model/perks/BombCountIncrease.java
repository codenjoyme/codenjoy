package com.codenjoy.dojo.bomberman.model.perks;

import com.codenjoy.dojo.bomberman.model.Elements;

public class BombCountIncrease extends Perk {
    public BombCountIncrease(int value, int timeout) {
        super(Elements.BOMB_COUNT_INCREASE, value, timeout);
    }

    @Override
    public void tick() {
        setTimer(getTimer() - 1);
    }

    /**
     * In case player grabs another such a perk while current one is active still,
     * timer will be reset to initial time-out value.
     */
    @Override
    public Perk combine(Perk perk) {
        return new BombCountIncrease(getValue(), getTimeout());
    }
}
