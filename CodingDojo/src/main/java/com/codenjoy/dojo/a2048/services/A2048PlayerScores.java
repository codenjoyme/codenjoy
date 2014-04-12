package com.codenjoy.dojo.a2048.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class A2048PlayerScores implements PlayerScores {

    private volatile int score;

    public A2048PlayerScores(int startScore) {
        this.score = startScore;
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object o) {
        A2048Events event = (A2048Events)o;

        if (event.getType() == A2048Events.Event.INC) {
            score += event.getNumber();
        }
        score = Math.max(0, score);
    }
}
