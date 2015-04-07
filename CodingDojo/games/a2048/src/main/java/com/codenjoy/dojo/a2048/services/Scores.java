package com.codenjoy.dojo.a2048.services;

import com.codenjoy.dojo.services.PlayerScores;

public class Scores implements PlayerScores {

    private volatile int score;

    public Scores(int startScore) {
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
        Events event = (Events)o;

        if (event.getType() == Events.Event.SUM) {
            if (event.getNumber() > score) {
                score = event.getNumber();
            }
        }
    }
}
