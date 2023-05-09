package com.codenjoy.dojo.startandjump.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.startandjump.TestGameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.startandjump.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.startandjump.services.GameSettings.Keys.WIN_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;

    private GameSettings settings;

    public void lose() {
        scores.event(Event.LOSE);
    }

    public void stillAlive() {
        scores.event(Event.STILL_ALIVE);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        stillAlive();
        stillAlive();
        stillAlive();
        stillAlive();

        lose();
        lose();

        // then
        assertEquals(140
                    + 4 * settings.integer(WIN_SCORE)
                    + 2 * settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        givenScores(0);

        // when
        lose();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);
        stillAlive();

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenStillAlive() {
        // given
        givenScores(140);

        // when
        stillAlive();
        stillAlive();

        // then
        assertEquals(140
                    + 2 *  settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        givenScores(140);

        // when
        lose();
        lose();

        // then
        assertEquals(140
                    + 2 *  settings.integer(LOSE_PENALTY),
                scores.getScore());
    }
}