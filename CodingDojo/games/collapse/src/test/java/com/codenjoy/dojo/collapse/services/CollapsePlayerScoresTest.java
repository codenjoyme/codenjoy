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

    public void success(int count) {
        CollapseEvents success = CollapseEvents.SUCCESS;
        success.setCount(count);
        scores.event(success);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new CollapsePlayerScores(0, settings);

        successScore = 2;
        settings.addEditBox("Success score").type(Integer.class).update(successScore);
    }

    @Test
    public void shouldCollectScores() {
        scores = new CollapsePlayerScores(1000, settings);

        success(1);
        success(1);
        success(1);
        success(1);

        assertEquals(1000 + 4*successScore, scores.getScore());
    }

    @Test
    public void shouldCollectScoresIfMoreThan1() {
        scores = new CollapsePlayerScores(1000, settings);

        success(5);

        assertEquals(1000 + (1+2+3+4+5)*successScore, scores.getScore());
    }

    @Test
    public void shouldCollectScoresIfMoreThan1_2() {
        scores = new CollapsePlayerScores(1000, settings);

        success(15);

        assertEquals(1000 + (1+2+3+4+5+6+7+8+9+10+11+12+13+14+15)*successScore, scores.getScore());
    }

}
