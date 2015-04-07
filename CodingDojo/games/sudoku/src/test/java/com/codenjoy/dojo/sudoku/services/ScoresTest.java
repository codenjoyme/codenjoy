package com.codenjoy.dojo.sudoku.services;

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

    private Integer failPenalty;
    private Integer winScore;
    private Integer successScore;
    private Integer loosePenalty;

    public void fail() {
        scores.event(Events.FAIL);
    }

    public void success() {
        scores.event(Events.SUCCESS);
    }

    public void win() {
        scores.event(Events.WIN);
    }

    private void loose() {
        scores.event(Events.LOOSE);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        winScore = settings.addEditBox("Win score").type(Integer.class).getValue();
        failPenalty = settings.addEditBox("Fail penalty").type(Integer.class).getValue();
        successScore = settings.addEditBox("Success score").type(Integer.class).getValue();
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        success();  //+10
        success();  //+10
        success();  //+10
        success();  //+10

        fail(); //-10

        win(); // +1000

        loose(); // -500

        assertEquals(140 + 4* successScore - failPenalty + winScore - loosePenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterFail() {
        fail();    //-10

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win();    // +30

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
