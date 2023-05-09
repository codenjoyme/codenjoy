package com.codenjoy.dojo.moebius.services;

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


import com.codenjoy.dojo.moebius.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.moebius.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.moebius.services.GameSettings.Keys.WIN_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void gameOver() {
        scores.event(new Event(Event.Type.GAME_OVER));
    }

    public void win(int lines) {
        scores.event(new Event(Event.Type.WIN, lines));
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        win(1);
        win(2);
        win(3);
        win(4);

        gameOver();

        // then
        assertEquals(140
                    + (1 + 2 + 3 + 4) * settings.integer(WIN_SCORE)
                    + settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        givenScores(0);

        // when
        gameOver();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);
        win(10);

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        givenScores(140);

        // when
        win(1);
        win(2);

        // then
        assertEquals(140
                    + (1 + 2) * settings.integer(WIN_SCORE)
                    + settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenGameOver() {
        // given
        givenScores(140);

        // when
        gameOver();
        gameOver();

        // then
        assertEquals(140
                    + 2 * settings.integer(LOSE_PENALTY),
                scores.getScore());
    }
}