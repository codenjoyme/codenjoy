package com.codenjoy.dojo.quadro.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
    private Integer winScore;
    private Integer looseScore;
    private Integer drawScore;

    private void win() {
        scores.event(Events.WIN);
    }

    private void loose() {
        scores.event(Events.LOOSE);
    }

    private void draw() {
        scores.event(Events.DRAW);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
        looseScore = settings.getParameter("Loose score").type(Integer.class).getValue();
        drawScore = settings.getParameter("Draw score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        win();  //+10
        win();  //+10
        win();  //+10
        draw(); //+3
        loose(); //+1

        assertEquals(140 + 3 * winScore + drawScore + looseScore, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win();    // +10

        scores.clear();

        assertEquals(0, scores.getScore());
    }
}
