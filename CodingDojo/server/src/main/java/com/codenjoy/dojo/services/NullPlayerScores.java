package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 22:31
 */
public class NullPlayerScores implements PlayerScores {

    public static final PlayerScores INSTANCE = new NullPlayerScores();

    private NullPlayerScores() {
        // do nothing
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public int clear() {
        return 0;
    }

    @Override
    public void event(Object event) {
        // do nothing
    }
}
