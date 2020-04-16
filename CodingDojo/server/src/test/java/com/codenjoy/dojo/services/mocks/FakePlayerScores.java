package com.codenjoy.dojo.services.mocks;

import com.codenjoy.dojo.services.PlayerScores;

public class FakePlayerScores implements PlayerScores {

    private Object score;

    public FakePlayerScores(Object score) {
        this.score = score;
    }

    @Override
    public Object getScore() {
        return score;
    }

    @Override
    public int clear() {
        return (int)(score = 0);
    }

    @Override
    public void update(Object score) {
        this.score = score;
    }

    @Override
    public void event(Object event) {

    }
}
