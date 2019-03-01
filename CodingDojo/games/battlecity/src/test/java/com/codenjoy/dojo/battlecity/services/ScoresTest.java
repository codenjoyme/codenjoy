package com.codenjoy.dojo.battlecity.services;

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
    private Integer killYourTankPenalty;
    private Integer killOtherHeroTankScore;
    private Integer killOtherAITankScore;

    public void killYourTank() {
        scores.event(Events.KILL_YOUR_TANK);
    }

    public void killOtherAITank() {
        scores.event(Events.KILL_OTHER_AI_TANK);
    }

    public void killOtherHeroTank(int amount) {
        scores.event(Events.KILL_OTHER_HERO_TANK.apply(amount));
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        killYourTankPenalty = settings.getParameter("Kill your tank penalty").type(Integer.class).getValue();
        killOtherAITankScore = settings.getParameter("Kill other AI tank score").type(Integer.class).getValue();
        killOtherHeroTankScore = settings.getParameter("Kill other hero tank score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        killOtherHeroTank(1);
        killOtherHeroTank(2);
        killOtherHeroTank(3);
        killOtherAITank();
        killOtherAITank();

        killYourTank();

        assertEquals(140
                + (1 + 2 + 3)*killOtherHeroTankScore
                + 2*killOtherAITankScore
                - killYourTankPenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        killYourTank();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        killOtherHeroTank(1);

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
