package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.a2048.services.A2048Events;
import com.codenjoy.dojo.services.EventListener;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(A2048Events event) {
        switch (event.getType()) {
            case GAME_OVER: gameOver(); break;
            case SUM: increaseScore(); break;
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