package com.globallogic.snake.services;

import com.globallogic.snake.model.middle.SnakeEventListener;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:22 AM
 */
public class PlayerScores implements SnakeEventListener {

    public static final int GAME_OVER_PENALTY = -500;
    public static final int EAT_APPLE_SCORE = 10;
    public static final int EAT_STONE_PENALTY = -100;

    private volatile int score;

    public PlayerScores(int startScore) {
        this.score = startScore;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void snakeIsDead() {
        score += GAME_OVER_PENALTY;
    }

    @Override
    public void snakeEatApple() {
        score += EAT_APPLE_SCORE;
    }

    @Override
    public void snakeEatStone() {
        score += EAT_STONE_PENALTY;
    }
}
