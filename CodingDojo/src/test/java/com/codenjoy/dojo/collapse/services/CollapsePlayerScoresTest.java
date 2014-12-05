package com.codenjoy.dojo.collapse.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CollapsePlayerScoresTest {
    private PlayerScores scores;

    private Settings settings;

    private Integer successScore;
    private Integer newGamePenalty;

    public void success() {
        scores.event(CollapseEvents.SUCCESS);
    }

    private void newGame() {
        scores.event(CollapseEvents.NEW_GAME);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new CollapsePlayerScores(0, settings);

        successScore = settings.addEditBox("Success score").type(Integer.class).getValue();
        newGamePenalty = settings.addEditBox("New game penalty").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new CollapsePlayerScores(1000, settings);

        success();
        success();
        success();
        success();

        newGame();

        assertEquals(1000 + 4*successScore - newGamePenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterFail() {
        newGame();

        assertEquals(0, scores.getScore());
    }

}
