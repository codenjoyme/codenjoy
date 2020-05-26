package com.codenjoy.dojo.bomberman.model.perks;

import com.codenjoy.dojo.bomberman.model.Elements;

public class BombRemoteControl extends Perk {
    public BombRemoteControl(int timeout) {
        super(Elements.BOMB_REMOTE_CONTROL, timeout);
    }

    @Override
    public Perk combine(Perk perk) {
        return new BombRemoteControl(getTimeout());
    }

    @Override
    // We don't countdown by time, only by usage
    public void tick() {
        setTimer(getTimeout());
    }
}
