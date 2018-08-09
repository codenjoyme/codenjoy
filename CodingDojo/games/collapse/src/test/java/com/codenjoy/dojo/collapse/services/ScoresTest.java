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


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoresTest {
    private PlayerScores scores;

    private Settings settings;

    private Integer successScore;

    public void success(int count) {
        Events success = Events.SUCCESS;
        success.setCount(count);
        scores.event(success);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        successScore = 2;
        settings.addEditBox("Success score").type(Integer.class).update(successScore);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(1000, settings);

        success(1);
        success(1);
        success(1);
        success(1);

        assertEquals(1000 + 4*successScore, scores.getScore());
    }

    @Test
    public void shouldCollectScoresIfMoreThan1() {
        scores = new Scores(1000, settings);

        success(5);

        assertEquals(1000 + (1+2+3+4+5)*successScore, scores.getScore());
    }

    @Test
    public void shouldCollectScoresIfMoreThan1_2() {
        scores = new Scores(1000, settings);

        success(15);

        assertEquals(1000 + (1+2+3+4+5+6+7+8+9+10+11+12+13+14+15)*successScore, scores.getScore());
    }

}
