package com.codenjoy.dojo.rubicscube.services;

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
public class RubicsCubePlayerScoresTest {
    private PlayerScores scores;

    private Settings settings;

    private Integer failPenalty;
    private Integer successScore;

    public void fail() {
        scores.event(RubicsCubeEvents.FAIL);
    }

    public void success() {
        scores.event(RubicsCubeEvents.SUCCESS);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new RubicsCubePlayerScores(0, settings);

        failPenalty = settings.addEditBox("Fail penalty").type(Integer.class).getValue();
        successScore = settings.addEditBox("Success score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new RubicsCubePlayerScores(140, settings);

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
