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

    public void success() {
        CollapseEvents success = CollapseEvents.SUCCESS;
        success.setCount(1);
        scores.event(success);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new CollapsePlayerScores(0, settings);

        successScore = settings.addEditBox("Success score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new CollapsePlayerScores(1000, settings);

        success();
        success();
        success();
        success();

        assertEquals(1000 + 4*successScore, scores.getScore());
    }

}
