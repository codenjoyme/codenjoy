package com.codenjoy.dojo.moebius.services;

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


import com.codenjoy.dojo.services.PlayerScores;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.moebius.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.moebius.services.GameSettings.Keys.WIN_SCORE;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void gameOver() {
        scores.event(new Events(Events.Event.GAME_OVER));
    }

    public void win(int lines) {
        scores.event(new Events(Events.Event.WIN, lines));
    }

    @Before
    public void setup() {
        settings = new GameSettings();
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        win(1);
        win(2);
        win(3);
        win(4);

        gameOver();

        assertEquals(140
                + (1+2+3+4) * settings.integer(WIN_SCORE)
                - settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        gameOver();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win(10);

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
