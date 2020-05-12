package com.codenjoy.dojo.bomberman.model.perks;

import com.codenjoy.dojo.bomberman.model.Elements;

public class BombImmune extends Perk {
    public BombImmune(int timeout) {
        super(Elements.BOMB_IMMUNE, 0, timeout);
    }


    /**
     * In case player grabs another such a perk while current one is active still,
     * timer will be reset to initial time-out value.
     */
    @Override
    public Perk combine(Perk perk) {
        return new BombImmune(getTimeout());
    }
}
