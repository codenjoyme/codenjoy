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
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoresTest {
    private static final int MOVE_COUNT = 2;
    private static final int NUMBER = 5;

    private PlayerScores scores;

    private Settings settings;
    private Integer winScore;
    private Integer bonusScore;

    public void win() {
        scores.event(Events.WIN);
    }

    public void bonus() {
        scores.event(new Bonus(MOVE_COUNT, NUMBER));
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
        bonusScore = settings.getParameter("Bonus score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(250, settings);

        bonus(); // + 100
        bonus(); // + 100

        Assert.assertEquals(250 + 2 * bonusScore * NUMBER / MOVE_COUNT, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenWin() {
        scores = new Scores(250, settings);

        win(); // + 1000

        Assert.assertEquals(250 + winScore, scores.getScore());
    }
}
