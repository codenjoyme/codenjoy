package com.codenjoy.dojo.hex.services;

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


import com.codenjoy.dojo.hex.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.hex.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.hex.services.GameSettings.Keys.WIN_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;

    private GameSettings settings;

    public void lose(int count) {
        scores.event(new Event(Event.Type.LOSE, count));
    }

    public void win(int count) {
        scores.event(new Event(Event.Type.WIN, count));
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Scores(settings));
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        win(1);
        win(1);
        win(1);
        win(2);

        lose(1);

        // then
        assertEquals(140
                    + 5 * settings.integer(WIN_SCORE)
                    + settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        givenScores(0);

        // when
        lose(1);

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);
        win(1);

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
                    + (1 + 2) * settings.integer(WIN_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        givenScores(140);

        // when
        lose(1);
        lose(2);

        // then
        assertEquals(140
                    + (1 + 2) * settings.integer(LOSE_PENALTY),
                scores.getScore());
    }
}