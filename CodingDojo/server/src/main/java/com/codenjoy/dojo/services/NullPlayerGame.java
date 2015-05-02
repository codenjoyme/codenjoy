package com.codenjoy.dojo.services;

public class NullPlayerGame extends PlayerGame {

    public static final PlayerGame INSTANCE = new NullPlayerGame();

    private NullPlayerGame() {
        super(NullPlayer.INSTANCE, NullGame.INSTANCE, NullPlayerController.INSTANCE, null);
    }
}
