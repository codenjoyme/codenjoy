package com.epam.dojo.icancode.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import junit.framework.Assert;
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
    private Integer goldScore;

    public void loose() {
        scores.event(new Events());
    }

    public void win() {
        scores.event(new Events(0));
    }

    public void win(int goldCount) {
        scores.event(new Events(goldCount));
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        loosePenalty = settings.getParameter("Loose penalty").type(Integer.class).getValue();
        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
        goldScore = settings.getParameter("Gold score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        win();  //+
        win();  //+
        win();  //+
        win();  //+

        loose(); //-

        Assert.assertEquals(140 + 4 * winScore - loosePenalty, scores.getScore());
    }

    @Test
    public void shouldWithWithGold() {
        scores = new Scores(0, settings);

        win(0);  //+
        win(1);  //+
        win(2);  //+

        Assert.assertEquals(3 * winScore + 3 * goldScore, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        loose();   //-

        Assert.assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win();    // +

        scores.clear();

        Assert.assertEquals(0, scores.getScore());
    }


}
