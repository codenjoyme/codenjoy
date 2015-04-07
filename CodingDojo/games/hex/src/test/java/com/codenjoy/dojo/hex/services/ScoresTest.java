package com.codenjoy.dojo.hex.services;

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
    private Integer loosePenalty;
    private Integer winScore;

    public void loose(int count) {
        scores.event(new Event(Event.EventEnum.LOOSE, count));
    }

    public void win(int count) {
        scores.event(new Event(Event.EventEnum.WIN, count));
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        loosePenalty = settings.getParameter("Loose penalty").type(Integer.class).getValue();
        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        win(1);  //+30
        win(1);  //+30
        win(1);  //+30
        win(2);  //+60

        loose(1); //-100

        assertEquals(140 + 5 * winScore - loosePenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        loose(1);    //-100

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win(1);    // +30

        scores.clear();

        assertEquals(0, scores.getScore());
    }


}
