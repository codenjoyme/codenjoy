package com.codenjoy.dojo.loderunner.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {
    private PlayerScores scores;

    private GameSettings settings;
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
        settings = new GameSettings()
                .integer(KILL_HERO_PENALTY, 30)
                .integer(GET_GOLD_SCORE, 10)
                .integer(GET_NEXT_GOLD_INCREMENT, 3);
        
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        killEnemy();  
        killEnemy();  
        killEnemy();  

        getGold();  

        killHero(); 

        assertEquals(140
                + 3 * settings.integer(KILL_ENEMY_SCORE)
                + settings.integer(GET_GOLD_SCORE)
                - settings.integer(KILL_HERO_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        killHero();    

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        getGold();

        scores.clear();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldIncreaseForNextGold() {
        scores = new Scores(0, settings);

        getGold();  
        getGold();
        getGold();
        getGold();

        assertEquals(4 * settings.integer(GET_GOLD_SCORE)
                + 3
                + 6
                + 9,
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfGameOver() {
        scores = new Scores(0, settings);

        getGold();  
        getGold();
        getGold();
        killHero(); 

        assertEquals(3 + 6, scores.getScore());

        getGold();  
        getGold();

        assertEquals(3 + 6 + 10 + 13, scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfClean() {
        scores = new Scores(0, settings);

        getGold();  
        getGold();  //+13
        getGold();  //+16
        scores.clear();

        assertEquals(0, scores.getScore());

        getGold();  
        getGold();  //+13

        assertEquals(10 + 13, scores.getScore());
    }


}
