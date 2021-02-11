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


import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class MaxScoresTest {

    private Scores scores;
    private Settings settings;

    private SnakeSettings setup;

    public void snakeEatApple() {
        scores.event(Events.EAT_APPLE);
    }

    public void snakeIsDead() {
        scores.event(Events.KILL);
    }

    public void snakeEatStone() {
        scores.event(Events.EAT_STONE);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        setup = new SnakeSettings(settings);
        scores = getScores(0);
    }

    private MaxScores getScores(int startScore) {
        return getScores(startScore, 2);
    }

    private MaxScores getScores(int startScore, int startLength) {
        return new MaxScores(startScore, setup){{
            length = startLength;
        }};
    }

    @Test
    public void shouldCollectScores() {
        // given
        scores = getScores(scoreFor(7), 7);

        // when
        snakeEatApple();  //+8

        // then
        assertEquals(8, length());
        assertEquals(scoreFor(8), score());

        // when
        snakeEatApple();  //+9

        // then
        assertEquals(9, length());
        assertEquals(scoreFor(9), score());

        // when
        snakeEatApple();  //+10

        // then
        assertEquals(10, length());
        assertEquals(scoreFor(10), score());

        // when
        snakeEatApple();  //+11

        // then
        assertEquals(11, length());
        assertEquals(scoreFor(11), score());

        // when
        snakeEatApple();  //+12

        // then
        assertEquals(12, length());
        assertEquals(scoreFor(12), score());

        // when
        snakeEatApple();  //+13

        // then
        assertEquals(13, length());
        assertEquals(scoreFor(13), score());
    }

    private int scoreFor(int length) {
        return IntStream.rangeClosed(3, length).sum();
    }

    @Test
    public void shouldSnakeLengthCantLessThen3() {
        // given
        scores = getScores(0);

        // when
        snakeEatStone();  //do nothing with score, just try length =- 10
        snakeEatStone();  //do nothing with score, just try length =- 10

        // then
        assertEquals(2, length());
        assertEquals(0, score());

        // when
        snakeEatApple();

        // then
        assertEquals(3, length());
        assertEquals(3, score());
    }

    @Test
    public void shouldShortLength_whenEatStone() {
        // given
        shouldCollectScores();
        assertEquals(13, length());
        assertEquals(scoreFor(13), score());

        // when
        snakeEatStone();  //do nothing with score, just length =- 10

        // then
        assertEquals(13 - 10, length());
        assertEquals(scoreFor(13), score());
    }

    @Test
    public void shouldShortLength_whenDead() {
        // given
        shouldCollectScores();
        assertEquals(13, length());
        assertEquals(scoreFor(13), score());

        // when
        snakeIsDead();    //do nothing with score, just length = 2

        // then
        assertEquals(2, length());
        assertEquals(scoreFor(13), score());
    }

    @Test
    public void shouldClearScoreTogetherWithSnakeLength() {
        // given
        scores = getScores(0);

        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6
        snakeEatApple();  //+7
        snakeEatApple();  //+8
        snakeEatApple();  //+9
        snakeEatApple();  //+10
        snakeEatApple();  //+11
        snakeEatApple();  //+12

        // when
        scores.clear();

        // then
        assertEquals(0, score());
        assertEquals(2, length());

        // when
        snakeEatApple();  //+3
        snakeEatApple();  //+4

        // then
        assertEquals(scoreFor(4), score());
        assertEquals(4, length());
    }

    @Test
    public void shouldStartsFromMaxScore_afterDead() {
        // given
        scores = getScores(100, 20);

        // when
        snakeIsDead();    //do nothing with score, just length = 2

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        // then
        assertEquals(100, score());
        assertEquals(4, length());
    }

    private int score() {
        return scores.getScore().intValue();
    }

    private int length() {
        return scores.getLength().intValue();
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        scores = getScores(0);

        // when
        snakeIsDead();    //do nothing with score, just length = 2

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldStillZero_afterEatStone() {
        // given
        scores = getScores(0);

        // when
        snakeEatStone();    //do nothing with score, just try length =- 10

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldClearScore() {
        // given
        scores = getScores(0);

        snakeEatApple();  //+3

        // when
        scores.clear();

        // then
        assertEquals(0, score());
    }
}
