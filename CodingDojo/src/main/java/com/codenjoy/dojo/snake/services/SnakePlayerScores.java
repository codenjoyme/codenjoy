package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SettingsImpl;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:22 AM
 */
public class SnakePlayerScores implements PlayerScores {

    private final Parameter<Integer> gameOverPenalty;
    private final Parameter<Integer> startSnakeLength;
    private final Parameter<Integer> eatStonePenalty;
    private final Parameter<Integer> eatStoneDecrease;

    private volatile int score;
    private volatile int length;  // TODO remove from here

    public SnakePlayerScores(int startScore, SettingsImpl parameters) {
        this.score = startScore;

        gameOverPenalty = parameters.addEditBox("Game over penalty").type(Integer.class).def(15);
        startSnakeLength = parameters.addEditBox("Start snake length").type(Integer.class).def(2);
        eatStonePenalty = parameters.addEditBox("Eat stone penalty").type(Integer.class).def(5);
        eatStoneDecrease = parameters.addEditBox("Eat stone decrease").type(Integer.class).def(10);

        length = startSnakeLength.getValue();

    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(SnakeEvents.KILL)) {
            snakeIsDead();
        } else if (event.equals(SnakeEvents.EAT_APPLE)) {
            snakeEatApple();
        }  else if (event.equals(SnakeEvents.EAT_STONE)) {
            snakeEatStone();
        }
        score = Math.max(0, score);
    }

    private void snakeIsDead() {
        score -= gameOverPenalty.getValue();
        length = startSnakeLength.getValue();
    }

    private void snakeEatApple() {
        length++;
        score += length;
    }

    private void snakeEatStone() {
        score -= eatStonePenalty.getValue();
        length -= eatStoneDecrease.getValue();
    }
}
