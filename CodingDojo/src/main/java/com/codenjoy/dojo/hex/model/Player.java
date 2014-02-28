package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.sample.services.SampleEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;

public class Player {

    private EventListener listener;
    private int maxScore;
    private int score;
    Hero hero;

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

    public void event(SampleEvents event) {
        switch (event) {
            case LOOSE: gameOver(); break;
            case WIN: increaseScore(); break;
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

    public Hero getHero() {
        return hero;
    }

    public void newHero(Field field) {
        Point pt = field.getFreeRandom();
        hero = new Hero(pt);
        hero.init(field);
    }

}