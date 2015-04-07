package com.codenjoy.dojo.a2048.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ScoresTest {
    private PlayerScores scores;

    private Settings settings;

    public void sum(int sum) {
        scores.event(new Events(Events.Event.SUM, sum));
    }

    @Before
    public void setup() {
        scores = new Scores(0);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(0);

        sum(10);
        sum(20);
        sum(30);

        assertEquals(30, scores.getScore());
    }

    @Test
    public void shouldNoCollectWhenLessThenMax() {
        scores = new Scores(40);

        sum(10);
        sum(20);
        sum(30);

        assertEquals(40, scores.getScore());
    }

    @Test
    public void shouldNoCollectWhenSame() {
        scores = new Scores(0);

        sum(10);
        sum(10);
        sum(10);

        assertEquals(10, scores.getScore());
    }


}
