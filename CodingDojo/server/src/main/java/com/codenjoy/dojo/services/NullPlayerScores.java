package com.codenjoy.dojo.services;

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
