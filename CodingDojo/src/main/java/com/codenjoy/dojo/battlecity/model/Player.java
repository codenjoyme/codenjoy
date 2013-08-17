package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.bomberman.model.Board;
import com.codenjoy.dojo.bomberman.model.Bomberman;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.Level;
import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;

public class Player {
    private Tank tank;
    private EventListener listener;
    private int maxScore;
    private int score;

    public Player(EventListener listener) {
        this.listener = listener;
        maxScore = 0;
        score = 0;
    }

    public Tank getTank() {
        return tank;
    }

    public void newGame() {
        tank = new Tank(1, 1, Direction.DOWN);
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
        tank.kill(null);
        score = 0;
    }
}