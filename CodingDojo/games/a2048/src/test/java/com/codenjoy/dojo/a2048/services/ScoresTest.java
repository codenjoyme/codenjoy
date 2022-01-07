package com.codenjoy.dojo.a2048.services;

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


import com.codenjoy.dojo.a2048.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void sum(int sum) {
        scores.event(new Event(Event.Type.SUM, sum));
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
        givenScores(0);

        // when
        sum(10);
        sum(20);
        sum(30);

        // then
        assertEquals(30, scores.getScore());
    }

    @Test
    public void shouldNoCollect_whenLessThenMax() {
        // given
        givenScores(40);

        // when
        sum(10);
        sum(20);
        sum(30);

        // then
        assertEquals(40, scores.getScore());
    }

    @Test
    public void shouldCollect_whenCumulative() {
        // given
        settings.initScore(CUMULATIVELY);

        givenScores(40);

        // when
        sum(10);
        sum(20);
        sum(30);

        // then
        assertEquals(100, scores.getScore());
    }

    @Test
    public void shouldNoCollect_whenSame() {
        // given
        givenScores(0);

        // when
        sum(10);
        sum(10);
        sum(10);

        // then
        assertEquals(10, scores.getScore());
    }
}