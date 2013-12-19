package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 19.12.13
 * Time: 4:55
 */
public class Ticker {

    private int timer;
    private Players players;

    public Ticker(Players players) {
        this.players = players;
    }

    public boolean collectTicks() {
        timer++;
        if (timer >= players.getCount()) {
            timer = 0;
        } else {
            return true;
        }
        return false;
    }
}
