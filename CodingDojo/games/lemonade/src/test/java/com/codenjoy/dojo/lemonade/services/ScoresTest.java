package com.codenjoy.dojo.lemonade.services;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.lemonade.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;

    private GameSettings settings;

    public void lose() {
        scores.event(new EventArgs(EventType.LOSE, 1, 0.3));
    }

    public void win() {
        scores.event(new EventArgs(EventType.WIN, 0.3, 0.3));
    }

    @Before
    public void setup() {
        settings = new GameSettings();
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        settings.integer(LIMIT_DAYS, 0); // sets SUM_OF_PROFITS scores counting mode

        scores = new Scores(140, settings);

        win();
        win();
        win();
        win();

        lose();

        Assert.assertEquals(140
                + 4 * settings.integer(WIN_SCORE)
                - settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        lose();

        Assert.assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win();

        scores.clear();

        Assert.assertEquals(0, scores.getScore());
    }


}
