package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 20:35
 */
public class ScoresTest {
    private PlayerScores scores;

    private Settings settings;
    private Integer killWall;
    private Integer killMeatChopper;
    private Integer killOtherBomnerman;
    private Integer killBomnerman;

    public void bombermanKillWall() {
        scores.event(Events.KILL_DESTROY_WALL);
    }

    public void bombermanKillBomberman() {
        scores.event(Events.KILL_BOMBERMAN);
    }

    public void bombermanKillMeatChopper() {
        scores.event(Events.KILL_MEAT_CHOPPER);
    }

    public void bombermanKillOtherBomberman() {
        scores.event(Events.KILL_OTHER_BOMBERMAN);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);
        killWall = settings.getParameter("Kill wall score").type(Integer.class).getValue();
        killMeatChopper = settings.getParameter("Kill meat chopper score").type(Integer.class).getValue();
        killOtherBomnerman = settings.getParameter("Kill other bomberman score").type(Integer.class).getValue();
        killBomnerman = settings.getParameter("Kill your bomberman penalty").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        bombermanKillWall();  //+10
        bombermanKillWall();  //+10
        bombermanKillWall();  //+10
        bombermanKillWall();  //+10

        bombermanKillBomberman();  //-50

        bombermanKillMeatChopper();  //100

        bombermanKillOtherBomberman();    //1000

        assertEquals(140 + 4*killWall - killBomnerman + killOtherBomnerman + killMeatChopper, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        bombermanKillBomberman();    //-50

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        bombermanKillWall();    // +10

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
