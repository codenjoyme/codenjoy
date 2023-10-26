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

import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.spacerace.TestGameSettings;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.spacerace.services.GameSettings.Keys.*;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(DESTROY_ENEMY_SCORE, 1)
                .integer(DESTROY_STONE_SCORE, 2)
                .integer(DESTROY_BOMB_SCORE, 3)
                .integer(LOSE_PENALTY, -1);
    }

    @Override
    protected Class<? extends ScoresMap> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "DESTROY_ENEMY > +1 = 101\n" +
                "DESTROY_ENEMY > +1 = 102\n" +
                "DESTROY_ENEMY > +1 = 103\n" +
                "DESTROY_ENEMY > +1 = 104\n" +
                "DESTROY_STONE > +2 = 106\n" +
                "DESTROY_STONE > +2 = 108\n" +
                "DESTROY_BOMB > +3 = 111\n" +
                "LOSE > -1 = 110");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        assertEvents("2:\n" +
                "LOSE > -1 = 1\n" +
                "LOSE > -1 = 0\n" +
                "LOSE > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("100:\n" +
                "DESTROY_ENEMY > +1 = 101\n" +
                "(CLEAN) > -101 = 0\n" +
                "DESTROY_BOMB > +3 = 3");
    }

    @Test
    public void shouldCollectScores_whenDestroyEnemy() {
        // given
        settings().integer(DESTROY_ENEMY_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "DESTROY_ENEMY > +1 = 101\n" +
                "DESTROY_ENEMY > +1 = 102");
    }

    @Test
    public void shouldCollectScores_whenDestroyStone() {
        // given
        settings().integer(DESTROY_STONE_SCORE, 2);

        // when then
        assertEvents("100:\n" +
                "DESTROY_STONE > +2 = 102\n" +
                "DESTROY_STONE > +2 = 104");
    }

    @Test
    public void shouldCollectScores_whenDestroyBomb() {
        // given
        settings().integer(DESTROY_BOMB_SCORE, 3);

        // when then
        assertEvents("100:\n" +
                "DESTROY_BOMB > +3 = 103\n" +
                "DESTROY_BOMB > +3 = 106");
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        settings().integer(LOSE_PENALTY, -1);

        // when then
        assertEvents("100:\n" +
                "LOSE > -1 = 99\n" +
                "LOSE > -1 = 98");
    }
}