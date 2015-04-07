package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
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
    private Integer killHeroPenalty;
    private Integer killEnemyScore;
    private Integer getGoldScore;
    private Integer forNextGoldIncScore;

    public void killHero() {
        scores.event(Events.KILL_HERO);
    }

    public void killEnemy() {
        scores.event(Events.KILL_ENEMY);
    }

    public void getGold() {
        scores.event(Events.GET_GOLD);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new Scores(0, settings);

        Parameter<Integer> p1 = settings.getParameter("Kill hero penalty").type(Integer.class);
        p1.update(30);
        killHeroPenalty = p1.getValue();

        killEnemyScore = settings.getParameter("Kill enemy score").type(Integer.class).getValue();

        Parameter<Integer> p3 = settings.getParameter("Get gold score").type(Integer.class);
        p3.update(10);
        getGoldScore = p3.getValue();

        Parameter<Integer> p2 = settings.getParameter("Get next gold increment score").type(Integer.class);
        p2.update(3);
        forNextGoldIncScore = p2.getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        killEnemy();  //+100
        killEnemy();  //+100
        killEnemy();  //+100

        getGold();  //+10

        killHero(); //-30

        assertEquals(140 + 3*killEnemyScore + getGoldScore - killHeroPenalty, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        killHero();    //-30

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        getGold();    // +10

        scores.clear();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldIncreaseForNextGold() {
        scores = new Scores(0, settings);

        getGold();  //+10
        getGold();  //+13
        getGold();  //+16
        getGold();  //+19

        assertEquals(10*4 + 3 + 6 + 9, scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfGameover() {
        scores = new Scores(0, settings);

        getGold();  //+10
        getGold();  //+13
        getGold();  //+16
        killHero(); //-30

        assertEquals(3 + 6, scores.getScore());

        getGold();  //+10
        getGold();  //+13

        assertEquals(3 + 6 + 10 + 13, scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfClean() {
        scores = new Scores(0, settings);

        getGold();  //+10
        getGold();  //+13
        getGold();  //+16
        scores.clear();

        assertEquals(0, scores.getScore());

        getGold();  //+10
        getGold();  //+13

        assertEquals(10 + 13, scores.getScore());
    }


}
