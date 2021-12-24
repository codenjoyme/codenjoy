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
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.snake.TestGameSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static com.codenjoy.dojo.services.event.ScoresImpl.Mode.MAX_VALUE;
import static org.junit.Assert.assertEquals;

public class MaxScoresTest {

    private PlayerScores scores;

    private GameSettings settings;

    public void eatApple(int length) {
        scores.event(Event.EAT_APPLE.apply(length));
    }

    public void kill() {
        scores.event(Event.KILL);
    }

    public void eatStone() {
        scores.event(Event.EAT_STONE);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
        settings.initScore(MAX_VALUE);

        scores = getScores(0);
    }

    private PlayerScores getScores(int score) {
        return new ScoresImpl<>(score, new Scores(settings));
    }

    @Test
    public void shouldCollectScores() {
        // given
        scores = getScores(scoreFor(7));

        // when
        eatApple(8);

        // then
        assertEquals(scoreFor(8), score());

        // when
        eatApple(9);

        // then
        assertEquals(scoreFor(9), score());

        // when
        eatApple(10);

        // then
        assertEquals(scoreFor(10), score());

        // when
        eatApple(11);

        // then
        assertEquals(scoreFor(11), score());

        // when
        eatApple(12);

        // then
        assertEquals(scoreFor(12), score());

        // when
        eatApple(13);

        // then
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
        eatStone();
        eatStone();

        // then
        assertEquals(0, score());

        // when
        eatApple(2);

        // then
        assertEquals(scoreFor(2), score());
    }

    @Test
    public void shouldShortLength_whenEatStone() {
        // given
        shouldCollectScores();
        assertEquals(scoreFor(13), score());

        // when
        eatStone();

        // then
        assertEquals(scoreFor(13), score());
    }

    @Test
    public void shouldShortLength_whenDead() {
        // given
        shouldCollectScores();
        assertEquals(scoreFor(13), score());

        // when
        kill();

        // then
        assertEquals(scoreFor(13), score());
    }

    @Test
    public void shouldClearScoreTogetherWithSnakeLength() {
        // given
        scores = getScores(0);

        eatApple(3);
        eatApple(4);
        eatApple(5);
        eatApple(6);
        eatApple(7);
        eatApple(8);
        eatApple(9);
        eatApple(10);
        eatApple(11);
        eatApple(12);

        // when
        scores.clear();

        // then
        assertEquals(0, score());

        // when
        eatApple(3);
        eatApple(4);

        // then
        assertEquals(scoreFor(4), score());
    }

    @Test
    public void shouldStartsFromMaxScore_afterDead() {
        // given
        scores = getScores(100);

        // when
        kill();

        eatApple(3);
        eatApple(4);

        // then
        assertEquals(100, score());
    }

    private int score() {
        return (int) scores.getScore();
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        scores = getScores(0);

        // when
        kill();

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldStillZero_afterEatStone() {
        // given
        scores = getScores(0);

        // when
        eatStone();

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldClearScore() {
        // given
        scores = getScores(0);

        eatApple(3);

        // when
        scores.clear();

        // then
        assertEquals(0, score());
    }
}