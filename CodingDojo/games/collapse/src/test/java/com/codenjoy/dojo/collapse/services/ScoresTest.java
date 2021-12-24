package com.codenjoy.dojo.collapse.services;

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


import com.codenjoy.dojo.collapse.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.collapse.services.Event.Type.SUCCESS;
import static com.codenjoy.dojo.collapse.services.GameSettings.Keys.SUCCESS_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void success(int count) {
        scores.event(new Event(SUCCESS, count));
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
        success(1);
        success(1);
        success(1);
        success(1);

        // then
        assertEquals(140
                    + 4 * settings.integer(SUCCESS_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScoresIfMoreThanOne_caseMultiple() {
        // given
        givenScores(140);

        // when
        success(5);
        success(5);

        // then
        assertEquals(140
                    + 2 * (1 + 2 + 3 + 4 + 5) * settings.integer(SUCCESS_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScoresIfMoreThanOne_caseSingle() {
        // given
        givenScores(140);

        // when
        success(15);

        // then
        assertEquals(140
                    + (1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12 + 13 + 14 + 15)
                        * settings.integer(SUCCESS_SCORE),
                scores.getScore());
    }
}