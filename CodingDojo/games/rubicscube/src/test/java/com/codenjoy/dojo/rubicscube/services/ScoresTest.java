package com.codenjoy.dojo.rubicscube.services;

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

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class ScoresTest {
    private PlayerScores scores;

    private Settings settings;

    private Integer failPenalty;
    private Integer successScore;

    public void fail() {
        scores.event(Events.FAIL);
    }

    public void success() {
        scores.event(Events.SUCCESS);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        failPenalty = settings.addEditBox("Fail penalty").type(Integer.class).getValue();
        successScore = settings.addEditBox("Success score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        success();  //+1000
        success();  //+1000
        success();  //+1000
        success();  //+1000

        fail(); //-500

        assertEquals(140 + 4* successScore - failPenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterFail() {
        fail();    //-500

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        success();    // +1000

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
