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


import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author K.ilya
 */
@RunWith(Parameterized.class)
public class ScoresTest {

    Scores scores;
    Events event;
    int changeValue;

    public ScoresTest(int startScore, Events event, int changeValue) {
        SettingsImpl settings = new SettingsImpl();
        scores = new Scores(startScore, settings);
        settings.getParameter("Win score").type(Integer.class).update(30);
        settings.getParameter("Apple score").type(Integer.class).update(1);
        settings.getParameter("Gold score").type(Integer.class).update(5);
        settings.getParameter("Die penalty").type(Integer.class).update(10);
        settings.getParameter("Stone score").type(Integer.class).update(-1);
        this.event = event;
        this.changeValue = changeValue;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] params = new Object[][]{
                {0, Events.START, 0},
                {0, Events.APPLE, +1},
                {0, Events.GOLD, +5},
                {0, Events.STONE, 0}, // счёт всегда >=0
                {0, Events.WIN, +30},
                {0, Events.DIE, 0}, // счёт всегда >=0
                {100, Events.START, 0},
                {100, Events.APPLE, +1},
                {100, Events.GOLD, +5},
                {100, Events.STONE, -1},
                {100, Events.WIN, +30},
                {100, Events.DIE, -10},
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
