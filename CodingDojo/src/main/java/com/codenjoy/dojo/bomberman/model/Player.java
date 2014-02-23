package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.EventListener;

public class Player {
    private Bomberman bomberman;
    private EventListener listener;
    private int maxScore;
    private int score;
    private GameSettings settings;

    public Player(EventListener listener) {
        this.listener = listener;
        clearScore();
    }

    public Bomberman getBomberman() {
        return bomberman;
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

    public void event(BombermanEvents event) {
        switch (event) {
            case KILL_MEAT_CHOPPER : increaseScore(); break;
            case KILL_DESTROY_WALL : increaseScore(); break;
            case KILL_BOMBERMAN: gameOver(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        bomberman.kill();
        score = 0;
    }

    public void clearScore() {
        score = 0;
        maxScore = 0;
    }

    public void newHero(Board board) {
        score = 0;
        settings = board.getSettings();
        bomberman = settings.getBomberman(settings.getLevel());
        bomberman.init(board);
    }
}