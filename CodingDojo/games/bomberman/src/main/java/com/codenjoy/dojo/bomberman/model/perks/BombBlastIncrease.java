package com.codenjoy.dojo.bomberman.model.perks;

import com.codenjoy.dojo.bomberman.model.Elements;

public class BombBlastIncrease extends Perk {
    public BombBlastIncrease(int value, int timeout) {
        super(Elements.BOMB_BLAST_RADIUS_INCREASE, value, timeout);
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
        return new BombBlastIncrease(getValue(), getTimeout());
    }
}
