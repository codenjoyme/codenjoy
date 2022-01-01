package com.codenjoy.dojo.snakebattle.services;

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


import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.snakebattle.TestGameSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static com.codenjoy.dojo.snakebattle.services.Event.Type.*;
import static com.codenjoy.dojo.snakebattle.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;


@RunWith(Parameterized.class)
public class ScoresTest {

    private GameSettings settings;
    private ScoresImpl scores;
    private Event event;
    private int changeValue;

    public ScoresTest(int score, Event event, int changeValue) {
        settings = new TestGameSettings()
                .integer(WIN_SCORE, 30)
                .integer(APPLE_SCORE, 1)
                .integer(GOLD_SCORE, 5)
                .integer(DIE_PENALTY, -10)
                .integer(STONE_SCORE, -1);
        givenScores(score);
        this.event = event;
        this.changeValue = changeValue;
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] params = new Object[][]{
                {0, new Event(START), 0},
                {0, new Event(APPLE), +1},
                {0, new Event(GOLD), +5},
                {0, new Event(STONE), 0}, // счёт всегда >=0
                {0, new Event(WIN), +30},
                {0, new Event(DIE), 0}, // счёт всегда >=0
                {100, new Event(START), 0},
                {100, new Event(APPLE), +1},
                {100, new Event(GOLD), +5},
                {100, new Event(STONE), -1},
                {100, new Event(WIN), +30},
                {100, new Event(DIE), -10},
        };
        return Arrays.asList(params);
    }

    @Test
    public void eventTest() {
        for (int i = 0; i < 2; i++) {
            int before = scores.getScore();
            scores.event(event);
            int after = scores.getScore();
            assertEquals("После события '" + event + "', счёт не корректен!",
                    before + changeValue, after);
        }
    }
}
