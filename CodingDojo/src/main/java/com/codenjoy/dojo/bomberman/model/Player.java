package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.EventListener;

public class Player {
    private Bomberman bomberman;
    private EventListener listener;
    private int maxScore;
    private int score;
    private GameSettings settings;

    public Player() {
    }

    public void init(GameSettings settings, EventListener listener) {
        this.settings = settings;
        maxScore = 0;
        score = 0;
        this.listener = listener;
    }

    public Bomberman getBomberman() {
        return bomberman;
    }

    public void newGame(Board board, Level level) {
        bomberman = settings.getBomberman(level);
        bomberman.init(board);
    }

    public void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(String name) {
        listener.event(name);
    }

    public void gameOver() {
        bomberman.kill();
        score = 0;
    }
}