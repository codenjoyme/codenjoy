package com.codenjoy.dojo.bomberman.services;

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
public class BombermanPlayerScoresTest {
    private PlayerScores scores;

    private SettingsImpl parameters;
    private Integer killWall;
    private Integer killMeatChopper;
    private Integer killOtherBomnerman;
    private Integer killBomnerman;

    public void bombermanKillWall() {
        scores.event(BombermanEvents.KILL_DESTROY_WALL);
    }

    public void bombermanKillBomberman() {
        scores.event(BombermanEvents.KILL_BOMBERMAN);
    }

    public void bombermanKillMeatChopper() {
        scores.event(BombermanEvents.KILL_MEAT_CHOPPER);
    }

    public void bombermanKillOtherBomberman() {
        scores.event(BombermanEvents.KILL_OTHER_BOMBERMAN);
    }

    @Before
    public void setup() {
        parameters = new SettingsImpl();
        scores = new BombermanPlayerScores(0, parameters);
        killWall = parameters.getParameter("Kill wall score").type(Integer.class).getValue();
        killMeatChopper = parameters.getParameter("Kill meat chopper score").type(Integer.class).getValue();
        killOtherBomnerman = parameters.getParameter("Kill other bomberman score").type(Integer.class).getValue();
        killBomnerman = parameters.getParameter("Kill your bomberman penalty").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new BombermanPlayerScores(140, parameters);

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
