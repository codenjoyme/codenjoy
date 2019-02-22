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

import java.util.concurrent.atomic.AtomicInteger;

public class Scores implements PlayerScores {

    private final Parameter<Integer> gameOverPenalty;
    private final Parameter<Integer> startSnakeLength;
    private final Parameter<Integer> eatStonePenalty;
    private final Parameter<Integer> eatStoneDecrease;

    private AtomicInteger score;
    private AtomicInteger length;  // TODO remove from here

    public Scores(int startScore, Settings settings) {
        score = new AtomicInteger(startScore);

        gameOverPenalty = settings.addEditBox("Game over penalty").type(Integer.class).def(0);
        startSnakeLength = settings.addEditBox("Start snake length").type(Integer.class).def(2);
        eatStonePenalty = settings.addEditBox("Eat stone penalty").type(Integer.class).def(0);
        eatStoneDecrease = settings.addEditBox("Eat stone decrease").type(Integer.class).def(10);

        length = new AtomicInteger(startSnakeLength.getValue());
    }

    @Override
    public int clear() {
        length.set(startSnakeLength.getValue());
        return score.getAndSet(0);
    }

    @Override
    public Integer getScore() {
        return score.get();
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
        score.set(Math.max(0, score.get()));
        length.set(Math.max(startSnakeLength.getValue(), length.get()));
    }

    private void snakeIsDead() {
        score.addAndGet(Math.negateExact(gameOverPenalty.getValue()));
        length.set(startSnakeLength.getValue());
    }

    private void snakeEatApple() {
        score.addAndGet(length.incrementAndGet());
    }

    private void snakeEatStone() {
        score.addAndGet(Math.negateExact(eatStonePenalty.getValue()));
        length.addAndGet(Math.negateExact(eatStoneDecrease.getValue()));
    }
}
