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

    public static final int GAME_OVER_PENALTY = 50;
    public static final int EAT_STONE_PENALTY = 10;
    public static final int START_SNAKE_LENGTH = 2;

    private volatile int score;
    private volatile int length;

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
//        if (score < 0) { // TODO think about
//            score = 0;
//        }
        length = START_SNAKE_LENGTH;
    }

    @Override
    public void snakeEatApple() {
        score += length;
        length++;
    }

    @Override
    public void snakeEatStone() {
        length -= 10;
        score -= EAT_STONE_PENALTY;
    }

    @Override
    public void levelChanged(int levelNumber, GameLevel level) {
        // TODO implement me
    }
}
