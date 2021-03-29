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
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.loderunner.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void killHero() {
        scores.event(Events.KILL_HERO);
    }

    public void killEnemy() {
        scores.event(Events.KILL_ENEMY);
    }

    public void yellowGold() {
        scores.event(Events.GET_YELLOW_GOLD);
    }

    public void redGold() {
        scores.event(Events.GET_RED_GOLD);
    }

    public void greenGold() {
        scores.event(Events.GET_GREEN_GOLD);
    }

    @Before
    public void setup() {
        settings = new GameSettings()
                .integer(KILL_HERO_PENALTY, 30)
                .integer(KILL_ENEMY_SCORE, 10)

                .integer(GOLD_SCORE_GREEN, 2)
                .integer(GOLD_SCORE_GREEN_INCREMENT, 1)

                .integer(GOLD_SCORE_YELLOW, 20)
                .integer(GOLD_SCORE_YELLOW_INCREMENT, 10)

                .integer(GOLD_SCORE_RED, 200)
                .integer(GOLD_SCORE_RED_INCREMENT, 100);
        
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        killEnemy();  
        killEnemy();  
        killEnemy();  

        yellowGold();
        redGold();
        redGold();
        redGold();
        greenGold();
        greenGold();
        yellowGold();
        yellowGold();
        yellowGold();
        yellowGold();

        killHero(); 

        assertEquals(140
                + 3 * settings.integer(KILL_ENEMY_SCORE)

                + 5 * settings.integer(GOLD_SCORE_YELLOW)
                + (1 + 2 + 3 + 4) * settings.integer(GOLD_SCORE_YELLOW_INCREMENT)

                + 3 * settings.integer(GOLD_SCORE_RED)
                + (1 + 2) * settings.integer(GOLD_SCORE_RED_INCREMENT)

                + 2 * settings.integer(GOLD_SCORE_GREEN)
                + (1) * settings.integer(GOLD_SCORE_GREEN_INCREMENT)

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
        // given
        yellowGold();
        redGold();
        redGold();
        greenGold();
        greenGold();
        greenGold();

        assertEquals(529, scores.getScore());
        assertEquals("Scores{score=529, red=200, green=3, yellow=10}",
                scores.toString());

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
        assertEquals("Scores{score=0, red=0, green=0, yellow=0}",
                scores.toString());
    }

    @Test
    public void shouldIncreaseForNextGold() {
        scores = new Scores(0, settings);

        yellowGold();
        yellowGold();
        yellowGold();
        yellowGold();

        assertEquals(4 * settings.integer(GOLD_SCORE_YELLOW)
                + (1 + 2 + 3) * settings.integer(GOLD_SCORE_YELLOW_INCREMENT),
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfGameOver() {
        // given
        scores = new Scores(0, settings);

        yellowGold();
        yellowGold();
        yellowGold();

        // when
        killHero(); 

        // then
        Integer score = (Integer) scores.getScore();
        assertEquals(3 * settings.integer(GOLD_SCORE_YELLOW)
                    + (1 + 2) * settings.integer(GOLD_SCORE_YELLOW_INCREMENT)
                    - settings.integer(KILL_HERO_PENALTY),
                (int)score);

        // when
        yellowGold();
        yellowGold();

        // then
        assertEquals(score
                        + 2 * settings.integer(GOLD_SCORE_YELLOW)
                        + 1 * settings.integer(GOLD_SCORE_YELLOW_INCREMENT),
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfClean() {
        // given
        scores = new Scores(0, settings);

        yellowGold();
        yellowGold();
        yellowGold();

        // when
        scores.clear();

        assertEquals(0, scores.getScore());

        // then
        yellowGold();
        yellowGold();

        assertEquals(2 * settings.integer(GOLD_SCORE_YELLOW)
                        + 1 * settings.integer(GOLD_SCORE_YELLOW_INCREMENT),
                scores.getScore());
    }


}
