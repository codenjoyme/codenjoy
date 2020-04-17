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

// Классический подсчет очков, где очки постоянно агреггируются от игре к игре.
// "Инкриз" очков рассчитывается из текущего размера змеи в момент съедания яблока.
// Тогда как камень укорачивает только длинну (-10), ровно как и суицид (до 2х).
// В нконтролирруемой по времени игре побеждает тот, кто дольше играл, если это не ок -
// стоит выбирать MaxScore стратегию подсчета очков.  Включается опция на админке
// путем установки настройки игры "Max score mode" в false.
public class Scores implements PlayerScores {

    protected volatile int score;
    protected volatile int length;  // TODO remove from here
    protected SnakeSettings setup;

    public Scores(int startScore, SnakeSettings setup) {
        this.setup = setup;
        score = startScore;
        initLength();
    }

    protected void initLength() {
        length = setup.startSnakeLength().getValue();
    }

    @Override
    public int clear() {
        initLength();
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    public Integer getLength() {
        return length;
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
        length = Math.max(setup.startSnakeLength().getValue(), length);
    }

    protected void snakeIsDead() {
        score -= setup.gameOverPenalty().getValue();
        initLength();
    }

    protected void snakeEatApple() {
        length++;
        score += length;
    }

    protected void snakeEatStone() {
        score -= setup.eatStonePenalty().getValue();
        length -= setup.eatStoneDecrease().getValue();
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
