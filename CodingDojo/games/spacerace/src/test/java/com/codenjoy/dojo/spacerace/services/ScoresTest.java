package com.codenjoy.dojo.spacerace.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.spacerace.TestGameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;

    private GameSettings settings;

    public void lose() {
        scores.event(Event.LOSE);
    }

    public void destroyEnemy() {
        scores.event(Event.DESTROY_ENEMY);
    }

    public void destroyBomb() {
        scores.event(Event.DESTROY_BOMB);
    }

    public void destroyStone() {
        scores.event(Event.DESTROY_STONE);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        destroyEnemy();
        destroyEnemy();
        destroyEnemy();
        destroyEnemy();

        destroyStone();
        destroyStone();

        destroyBomb();

        lose();

        // then
        assertEquals(140
                    + 4 *  settings.integer(DESTROY_ENEMY_SCORE)
                    + 2 *  settings.integer(DESTROY_STONE_SCORE)
                    + settings.integer(DESTROY_BOMB_SCORE)
                    + settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        givenScores(0);

        // when
        lose();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCleanScore() {
        // given
        givenScores(0);
        destroyEnemy();

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenDestroyEnemy() {
        // given
        givenScores(140);

        // when
        destroyEnemy();
        destroyEnemy();

        // then
        assertEquals(140
                    + 2 *  settings.integer(DESTROY_ENEMY_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenDestroyStone() {
        // given
        givenScores(140);

        // when
        destroyStone();
        destroyStone();

        // then
        assertEquals(140
                    + 2 *  settings.integer(DESTROY_STONE_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenDestroyBomb() {
        // given
        givenScores(140);

        // when
        destroyBomb();
        destroyBomb();

        // then
        assertEquals(140
                    + 2 * settings.integer(DESTROY_BOMB_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        givenScores(140);

        // when
        lose();
        lose();

        // then
        assertEquals(140
                    + 2 * settings.integer(LOSE_PENALTY),
                scores.getScore());
    }
}