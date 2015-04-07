package com.codenjoy.dojo.chess.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Ignore;
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
    private Integer winScore;

    public void win() {
        scores.event(Events.WIN);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
    }

    @Test
    @Ignore
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        win();  //+30
        win();  //+30
        win();  //+30
        win();  //+30

        assertEquals(140 + 4* winScore, scores.getScore());
    }

    @Test
    @Ignore
    public void shouldClearScore() {
        win();    // +30

        scores.clear();

        assertEquals(0, scores.getScore());
    }

}
