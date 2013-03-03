package com.globallogic.snake.services;

import com.globallogic.snake.model.ChangeLevelListener;
import com.globallogic.snake.model.GameLevel;
import com.globallogic.snake.model.middle.SnakeEventListener;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:22 AM
 */
public class PlayerScores implements SnakeEventListener, ChangeLevelListener {

    public static final int GAME_OVER_PENALTY = 15;
    public static final int EAT_STONE_PENALTY = 5;
    public static final int START_SNAKE_LENGTH = 2;

    private volatile int score;
    private volatile int length;  // TODO remove from here

    public PlayerScores(int startScore) {
        this.score = startScore;
        length = START_SNAKE_LENGTH;
    }

    public int getScore() {
        return score;
    }

    @Override
    public void snakeIsDead() {
        score -= GAME_OVER_PENALTY;
        if (score < 0) {
            score = 0;
        }
        length = START_SNAKE_LENGTH;
    }

    @Override
    public void snakeEatApple() {
        length++;
        score += length;
    }

    @Override
    public void snakeEatStone() {
        score -= EAT_STONE_PENALTY;
        length -= 10;
        if (score < 0) {
            score = 0;
        }
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        // TODO implement me
    }
}
