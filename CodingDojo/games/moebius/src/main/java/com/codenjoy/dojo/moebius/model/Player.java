package com.codenjoy.dojo.moebius.model;

import com.codenjoy.dojo.moebius.services.Events;
import com.codenjoy.dojo.services.EventListener;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    private void increaseScore(int lines) {
        score = score + lines; // TODO посчитать лучше
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(Events event) {
        switch (event.getType()) {
            case GAME_OVER: gameOver(); break;
            case WIN: increaseScore(event.getLines()); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        score = 0;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

}