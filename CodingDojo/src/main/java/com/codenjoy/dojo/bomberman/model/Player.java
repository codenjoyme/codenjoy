package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.EventListener;

import static com.codenjoy.dojo.bomberman.services.BombermanEvents.KILL_BOMBERMAN;
import static com.codenjoy.dojo.bomberman.services.BombermanEvents.KILL_MEAT_CHOPPER;

public class Player {
    private Bomberman bomberman;
    private EventListener listener;
    private int maxScore;
    private int score;
    private GameSettings settings;

    public Player(EventListener listener) {
        this.listener = listener;
    }

    public void init(GameSettings settings) {
        this.settings = settings;
        maxScore = 0;
        score = 0;
    }

    public Bomberman getBomberman() {
        return bomberman;
    }

    public void newGame(Board board, Level level) {
        bomberman = settings.getBomberman(level);
        bomberman.init(board);
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
            listener.event(event.name());
        }
    }

    private void gameOver() {
        bomberman.kill();
        score = 0;
    }
}