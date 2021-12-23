package com.codenjoy.dojo.fifteen.services;

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

import com.codenjoy.dojo.fifteen.model.Bonus;
import com.codenjoy.dojo.services.PlayerScores;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.fifteen.services.GameSettings.Keys.BONUS_SCORE;
import static com.codenjoy.dojo.fifteen.services.GameSettings.Keys.WIN_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private static final int MOVE_COUNT = 2;
    private static final int NUMBER = 5;

    private PlayerScores scores;
    private GameSettings settings;

    public void win() {
        scores.event(Event.WIN);
    }

    public void bonus() {
        scores.event(new Bonus(MOVE_COUNT, NUMBER));
    }

    @Before
    public void setup() {
        settings = new GameSettings();
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(250, settings);

        bonus();
        bonus();

        assertEquals(250
                + 2 * settings.integer(BONUS_SCORE) * NUMBER / MOVE_COUNT,
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenWin() {
        scores = new Scores(250, settings);

        win();

        assertEquals(250 + settings.integer(WIN_SCORE),
                scores.getScore());
    }
}
