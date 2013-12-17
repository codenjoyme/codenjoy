package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class BattlecityPlayerScoresTest {
    private PlayerScores scores;

    private SettingsImpl parameters;
    private Integer killYourTankPenalty;
    private Integer killOtherTankScore;

    public void killYourTank() {
        scores.event(BattlecityEvents.KILL_YOUR_TANK);
    }

    public void killOtherTank() {
        scores.event(BattlecityEvents.KILL_OTHER_TANK);
    }

    @Before
    public void setup() {
        parameters = new SettingsImpl();
        scores = new BattlecityPlayerScores(0, parameters);

        killYourTankPenalty = parameters.getParameter("Kill your tank penalty").type(Integer.class).getValue();
        killOtherTankScore = parameters.getParameter("Kill other tank score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new BattlecityPlayerScores(140, parameters);

        killOtherTank();  //+100
        killOtherTank();  //+100
        killOtherTank();  //+100

        killYourTank();  //-50

        assertEquals(140 + 3*killOtherTankScore - killYourTankPenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        killYourTank();    //-50

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        killOtherTank();    // +10

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
