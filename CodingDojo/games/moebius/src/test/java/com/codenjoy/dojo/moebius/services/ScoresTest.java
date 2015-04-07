package com.codenjoy.dojo.moebius.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class ScoresTest {
//    private PlayerScores scores;
//
//    private Settings settings;
//    private Integer loosePenalty;
//    private Integer winScore;
//
//    public void gameOver() {
//        scores.event(Events.Event.GAME_OVER);
//    }
//
//    public void win() {
//        scores.event(Events.Event.WIN);
//    }
//
//    @Before
//    public void setup() {
//        settings = new SettingsImpl();
//        scores = new Scores(0, settings);
//
//        loosePenalty = settings.getParameter("Loose penalty").type(Integer.class).getValue();
//        winScore = settings.getParameter("Win score").type(Integer.class).getValue();
//    }
//
//    @Test
//    public void shouldCollectScores() {
//        scores = new Scores(140, settings);
//
//        win();  //+30
//        win();  //+30
//        win();  //+30
//        win();  //+30
//
//        gameOver(); //-100
//
//        Assert.assertEquals(140 + 4 * winScore - loosePenalty, scores.getScore());
//    }
//
//    @Test
//    public void shouldStillZeroAfterDead() {
//        gameOver();    //-100
//
//        Assert.assertEquals(0, scores.getScore());
//    }
//
//    @Test
//    public void shouldClearScore() {
//        win();    // +30
//
//        scores.clear();
//
//        Assert.assertEquals(0, scores.getScore());
//    }
//

}
