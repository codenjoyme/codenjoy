package com.codenjoy.dojo.snake.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private final Parameter<Integer> gameOverPenalty;
    private final Parameter<Integer> startSnakeLength;
    private final Parameter<Integer> eatStonePenalty;
    private final Parameter<Integer> eatStoneDecrease;

    private volatile int score;
    private volatile int length;  // TODO remove from here

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        gameOverPenalty = settings.addEditBox("Game over penalty").type(Integer.class).def(0);
        startSnakeLength = settings.addEditBox("Start snake length").type(Integer.class).def(2);
        eatStonePenalty = settings.addEditBox("Eat stone penalty").type(Integer.class).def(0);
        eatStoneDecrease = settings.addEditBox("Eat stone decrease").type(Integer.class).def(10);

        length = startSnakeLength.getValue();
    }

    @Override
    public int clear() {
        length = startSnakeLength.getValue();
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.KILL)) {
            snakeIsDead();
        } else if (event.equals(Events.EAT_APPLE)) {
            snakeEatApple();
        }  else if (event.equals(Events.EAT_STONE)) {
            snakeEatStone();
        }
        score = Math.max(0, score);
        length = Math.max(startSnakeLength.getValue(), length);
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

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
