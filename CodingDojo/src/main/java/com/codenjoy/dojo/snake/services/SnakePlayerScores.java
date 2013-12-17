package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.PlayerScores;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:22 AM
 */
public class SnakePlayerScores implements PlayerScores {

    public static final int GAME_OVER_PENALTY = 15;
    public static final int EAT_STONE_PENALTY = 5;
    public static final int START_SNAKE_LENGTH = 2;

    private volatile int score;
    private volatile int length;  // TODO remove from here

    public SnakePlayerScores(int startScore) {
        this.score = startScore;
        length = START_SNAKE_LENGTH;
    }

    @Override
    public int clear() { // TODO test me
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(SnakeEvents.KILL)) {  // TODO fixme
            snakeIsDead();
        } else if (event.equals(SnakeEvents.EAT_APPLE)) {
            snakeEatApple();
        }  else if (event.equals(SnakeEvents.EAT_STONE)) {
            snakeEatStone();
        }
    }

    private void snakeIsDead() {
        score -= GAME_OVER_PENALTY;
        if (score < 0) {
            score = 0;
        }
        length = START_SNAKE_LENGTH;
    }

    private void snakeEatApple() {
        length++;
        score += length;
    }

    private void snakeEatStone() {
        score -= EAT_STONE_PENALTY;
        length -= 10;
        if (score < 0) {
            score = 0;
        }
    }
}
