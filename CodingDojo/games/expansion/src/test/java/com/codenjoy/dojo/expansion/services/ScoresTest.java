package com.codenjoy.dojo.expansion.services;

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


import org.json.JSONObject;
import org.junit.Test;

import static com.codenjoy.dojo.expansion.services.Scores.DETAILS;
import static com.codenjoy.dojo.expansion.services.Scores.SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private Scores scores;

    public void lose() {
        scores.event(Events.LOSE());
    }

    public void win(int goldCount) {
        scores.event(Events.WIN(goldCount));
    }

    @Test
    public void shouldCollectScores() {
        // given
        scores = new Scores("{'score':140}");

        // when
        win(1);  //+
        win(1);  //+
        win(1);  //+
        win(1);  //+

        lose();  //-

        // then
        assertEquals(140 + 4 - 0, score());
    }

    private int score() {
        return scores.getScore().getInt(SCORE);
    }

    private String details() {
        return scores.getScore().getString(DETAILS);
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        scores = new Scores("{'score':0}");

        // when
        lose();   //-

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldClearScore() {
        // given
        scores = new Scores("{'score':0}");
        win(1);    // +

        // when
        scores.clear();

        // then
        assertEquals(0, score());
    }

    @Test
    public void testUpdate() {
        // given
        scores = new Scores("{'score':0}");

        // when then
        scores.update(new Object());
        assertScore(0);

        // when then
        scores.update("1");
        assertScore(1);

        // when then
        scores.update(2);
        assertScore(2);

        // when then
        scores.update("{\"score\":3,\"rounds\":[0]}");
        assertScore(3);
        assertRounds("[0]");

        // when then
        scores.update(new JSONObject("{\"score\":4,\"rounds\":[1,2]}"));
        assertScore(4);
        assertRounds("[1,2]");

    }

    private void assertScore(int expected) {
        assertEquals(expected, scores.getScore().getInt(SCORE));
    }

    private void assertRounds(String expected) {
        assertEquals(expected, scores.getScore().getJSONArray(Scores.ROUNDS).toString());
    }

    @Test
    public void shouldPrintDetails() {
        // given
        scores = new Scores("{'score':140}");

        // when
        win(1);

        // then
        assertEquals("25.00%(∑141)[i1]1", details());

        // when
        lose();

        // then
        assertEquals("12.50%(∑141)[i2]01", details());

        // when
        win(4);

        // then
        assertEquals("41.67%(∑145)[i3]401", details());

        // when
        win(3);

        // then
        assertEquals("50.00%(∑148)[i4]3401", details());

        // when
        lose();

        // then
        assertEquals("40.00%(∑148)[i5]03401", details());

        // when
        win(2);

        // then
        assertEquals("41.67%(∑150)[i6]203401", details());

        // when
        lose();

        // then
        assertEquals("35.71%(∑150)[i7]0203401", details());
    }
}
