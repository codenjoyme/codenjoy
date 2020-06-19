package com.codenjoy.dojo.bomberman.services;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class ScoresTest {
    private PlayerScores scores;

    private OptionGameSettings settings;

    public void killWall() {
        scores.event(Events.KILL_DESTROY_WALL);
    }

    public void killYourself() {
        scores.event(Events.DIED);
    }

    public void killMeatChopper() {
        scores.event(Events.KILL_MEAT_CHOPPER);
    }

    public void killOtherHero() {
        scores.event(Events.KILL_OTHER_HERO);
    }

    public void dropPerk() {
        scores.event(Events.CATCH_PERK);
    }

    public void winRound() {
        scores.event(Events.WIN_ROUND);
    }

    @Before
    public void setup() {
        settings = new OptionGameSettings(new SettingsImpl(), mock(Dice.class));
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        killWall();  //+10
        killWall();  //+10
        killWall();  //+10
        killWall();  //+10

        killYourself();  //-50

        killMeatChopper();  //100

        killOtherHero(); //200

        dropPerk(); //5

        winRound(); //1000

        assertEquals(140
                + 4*settings.killWallScore().getValue()
                - settings.diePenalty().getValue()
                + settings.catchPerkScore().getValue()
                + settings.killOtherHeroScore().getValue()
                + settings.killMeatChopperScore().getValue()
                + settings.winRoundScore().getValue(), scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        killYourself();    //-50

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        killWall();    // +10

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
