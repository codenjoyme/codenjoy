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

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class ScoresTest {
    private Scores scores;

    public void loose() {
        scores.event(Events.LOOSE());
    }

    public void win(int goldCount) {
        scores.event(Events.WIN(goldCount));
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores("{'score':140}");

        win(1);  //+
        win(1);  //+
        win(1);  //+
        win(1);  //+

        loose(); //-

        assertEquals(140 + 4 - 0, score());
    }

    private int score() {
        return scores.getScore().getInt("score");
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new Scores("{'score':0}");

        loose();   //-

        assertEquals(0, score());
    }

    @Test
    public void shouldClearScore() {
        scores = new Scores("{'score':0}");

        win(1);    // +

        scores.clear();

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
        assertEquals(expected, scores.getScore().getInt(Scores.SCORE));
    }

    private void assertRounds(String expected) {
        assertEquals(expected, scores.getScore().getJSONArray(Scores.ROUNDS).toString());
    }

}
