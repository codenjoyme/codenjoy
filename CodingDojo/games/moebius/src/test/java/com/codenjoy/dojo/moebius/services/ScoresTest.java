package com.codenjoy.dojo.moebius.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScoresTest {
    private PlayerScores scores;

    private Settings settings;
    private Integer loosePenalty;
    private Integer winScore;

    public void gameOver() {
        scores.event(new Events(Events.Event.GAME_OVER));
    }

    public void win(int lines) {
        scores.event(new Events(Events.Event.WIN, lines));
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

        win(1);  //+1*1
        win(2);  //+1*2
        win(3);  //+1*3
        win(4);  //+1*4

        gameOver(); //-100

        Assert.assertEquals(140 + (1+2+3+4) * winScore - loosePenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        gameOver();    //-100

        Assert.assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win(10);    // +1*10

        scores.clear();

        Assert.assertEquals(0, scores.getScore());
    }


}
