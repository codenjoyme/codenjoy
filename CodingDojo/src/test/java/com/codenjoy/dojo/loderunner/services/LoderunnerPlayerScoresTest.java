package com.codenjoy.dojo.loderunner.services;

import com.codenjoy.dojo.battlecity.services.BattlecityPlayerScores;
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
public class LoderunnerPlayerScoresTest {
    private PlayerScores scores;

    private Settings settings;
    private Integer killHeroPenalty;
    private Integer killEnemyScore;
    private Integer getGoldScore;

    public void killHero() {
        scores.event(LoderunnerEvents.KILL_HERO);
    }

    public void killEnemy() {
        scores.event(LoderunnerEvents.KILL_ENEMY);
    }

    public void getGold() {
        scores.event(LoderunnerEvents.GET_GOLD);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        scores = new LoderunnerPlayerScores(0, settings);

        killHeroPenalty = settings.getParameter("Kill hero penalty").type(Integer.class).getValue();
        killEnemyScore = settings.getParameter("Kill enemy score").type(Integer.class).getValue();
        getGoldScore = settings.getParameter("Get gold score").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new LoderunnerPlayerScores(140, settings);

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


}
