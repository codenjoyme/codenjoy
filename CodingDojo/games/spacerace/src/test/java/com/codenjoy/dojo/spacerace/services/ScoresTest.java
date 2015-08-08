package com.codenjoy.dojo.spacerace.services;

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
    private Integer removeEnemyScore;

    public void loose() {
        scores.event(Events.LOOSE);
    }

    public void destroyEnemy() {
        scores.event(Events.DESTROY_ENEMY);
    }
    // TODO implement for other cases

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        loosePenalty = settings.getParameter("Loose penalty").type(Integer.class).getValue();
        removeEnemyScore = settings.getParameter("Destroy enemy score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        destroyEnemy();  //+500
        destroyEnemy();  //+500
        destroyEnemy();  //+500
        destroyEnemy();  //+500

        loose(); //-100

        Assert.assertEquals(140 + 4 * removeEnemyScore - loosePenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        loose();    //-100

        Assert.assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        destroyEnemy();    // +30

        scores.clear();

        Assert.assertEquals(0, scores.getScore());
    }


}
