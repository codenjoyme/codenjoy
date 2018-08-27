package com.codenjoy.dojo.services.nullobj;

import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class NullGamePlayer extends GamePlayer {

    public static final GamePlayer INSTANCE = new NullGamePlayer();

    public NullGamePlayer(){
        super(event -> {});
    }

    @Override
    public PlayerHero getHero() {
        return NullPlayerHero.INSTANCE;
    }

    @Override
    public void newHero(GameField field) {
        // do nothing
    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
