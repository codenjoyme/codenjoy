package com.codenjoy.dojo.a2048.services;

import com.codenjoy.dojo.battlecity.services.BattlecityEvents;
import com.codenjoy.dojo.battlecity.services.BattlecityPlayerScores;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class A2048PlayerScoresTest {
    private PlayerScores scores;

    private Settings settings;

    public void sum(int sum) {
        scores.event(new A2048Events(A2048Events.Event.SUM, sum));
    }

    @Before
    public void setup() {
        scores = new A2048PlayerScores(0);
    }

    @Test
    public void shouldCollectScores() {
        scores = new A2048PlayerScores(0);

        sum(10);
        sum(20);
        sum(30);

        assertEquals(30, scores.getScore());
    }

    @Test
    public void shouldNoCollectWhenLessThenMax() {
        scores = new A2048PlayerScores(40);

        sum(10);
        sum(20);
        sum(30);

        assertEquals(40, scores.getScore());
    }

    @Test
    public void shouldNoCollectWhenSame() {
        scores = new A2048PlayerScores(0);

        sum(10);
        sum(10);
        sum(10);

        assertEquals(10, scores.getScore());
    }


}
