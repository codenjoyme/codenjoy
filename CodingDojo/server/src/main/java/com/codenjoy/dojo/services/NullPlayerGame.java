package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 22:20
 */
public class NullPlayerGame extends PlayerGame {

    public static final PlayerGame INSTANCE = new NullPlayerGame();

    private NullPlayerGame() {
        super(NullPlayer.INSTANCE, NullGame.INSTANCE, NullPlayerController.INSTANCE, null);
    }
}
