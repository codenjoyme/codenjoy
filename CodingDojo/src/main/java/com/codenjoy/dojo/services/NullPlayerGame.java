package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 22:20
 */
public class NullPlayerGame extends PlayerGame {

    NullPlayerGame() {
        super(Player.NULL, Game.NULL, PlayerController.NULL);
    }
}
