package com.codenjoy.dojo.football.services;

import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.football.services.Scores;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;


/**
 */
public class ScoresTest {
    private PlayerScores scores;

    private Settings settings;
    //private Integer loosePenalty;
    private Integer winScore;

    public void loose() {
        scores.event(Events.BOTTOM_GOAL);
    }

    public void win() {
        scores.event(Events.WIN);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        //loosePenalty = settings.getParameter("Loose penalty").type(Integer.class).getValue();
        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(1, settings);

        win();  //+1
        win();  //+1
        win();  //+1
        win();  //+1

        loose(); //-1

        Assert.assertEquals(1 + 4 * winScore, scores.getScore());
    }

    @Test
    public void cantBeLessThanZero() {
        loose();    //-100

        Assert.assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win();    // +1

        scores.clear();

        Assert.assertEquals(0, scores.getScore());
    }


}
