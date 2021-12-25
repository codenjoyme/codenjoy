package com.codenjoy.dojo.quake2d.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.quake2d.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.quake2d.services.Event.INJURE;
import static com.codenjoy.dojo.quake2d.services.Event.KILL;
import static com.codenjoy.dojo.quake2d.services.GameSettings.Keys.INJURE_SCORE;
import static com.codenjoy.dojo.quake2d.services.GameSettings.Keys.KILL_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    private void kill() {
        scores.event(KILL);
    }

    private void injure() {
        scores.event(INJURE);
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
        kill();
        kill();
        kill();

        injure();

        // then
        assertEquals(140
                    + 3 * settings.integer(KILL_SCORE)
                    + settings.integer(INJURE_SCORE),
                scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldStillZero_ifLessThan0() {
        // given
        settings.integer(INJURE_SCORE, -10);
        givenScores(0);

        // when
        injure();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);
        kill();

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenKill() {
        // given
        givenScores(140);

        // when
        kill();
        kill();

        // then
        assertEquals(140
                    + 2 * settings.integer(KILL_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenInjure() {
        // given
        givenScores(140);

        // when
        injure();
        injure();

        // then
        assertEquals(140
                    + 2 * settings.integer(INJURE_SCORE),
                scores.getScore());
    }
}