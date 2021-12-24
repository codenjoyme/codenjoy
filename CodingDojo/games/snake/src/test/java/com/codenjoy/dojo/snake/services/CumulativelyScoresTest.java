package com.codenjoy.dojo.snake.services;

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
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.snake.TestGameSettings;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.event.ScoresImpl.Mode.CUMULATIVELY;
import static com.codenjoy.dojo.snake.services.GameSettings.Keys.EAT_STONE_PENALTY;
import static com.codenjoy.dojo.snake.services.GameSettings.Keys.GAME_OVER_PENALTY;
import static org.junit.Assert.assertEquals;

public class CumulativelyScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void eatApple(int length) {
        scores.event(Event.EAT_APPLE.apply(length));
    }

    public void kill() {
        scores.event(Event.KILL);
    }

    public void eatStone() {
        scores.event(Event.EAT_STONE);
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
        settings.initScore(CUMULATIVELY);

        givenScores(0);
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(240);

        // when
        eatApple(3);
        eatApple(4);
        eatApple(5);
        eatApple(6);

        eatStone();

        kill();

        // then
        assertEquals(240
                        + 3 + 4 + 5 + 6
                        + settings.integer(EAT_STONE_PENALTY)
                        + settings.integer(GAME_OVER_PENALTY),
                score());
    }

    @Test
    public void shouldSnakeLengthCantLessThen3() {
        // given
        givenScores(0);

        // when
        eatStone();
        eatStone();

        // then
        assertEquals(0, score());

        // when
        eatApple(3);

        // then
        assertEquals(3, score());
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Scores(settings));
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        // given
        givenScores(0);

        eatApple(3);
        eatApple(4);
        eatApple(5);
        eatApple(6);
        eatApple(7);
        eatApple(8);
        eatApple(9);
        eatApple(10);
        eatApple(11);
        eatApple(12);

        // when
        eatStone();

        eatApple(3);
        eatApple(4);

        // then
        assertEquals(3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12
                + settings.integer(EAT_STONE_PENALTY)
                + 3 + 4, score());
    }

    @Test
    public void shouldClearScoreTogetherWithSnakeLength() {
        // given
        givenScores(0);

        eatApple(3);
        eatApple(4);
        eatApple(5);
        eatApple(6);
        eatApple(7);
        eatApple(8);
        eatApple(9);
        eatApple(10);
        eatApple(11);
        eatApple(12);

        // when
        scores.clear();

        eatApple(3);
        eatApple(4);

        // then
        assertEquals(3 + 4, score());
    }

    @Test
    public void shouldStartsFrom3_afterDead() {
        // given
        givenScores(200);

        // when
        kill();

        eatApple(3);
        eatApple(4);

        // then
        assertEquals(200
                + settings.integer(GAME_OVER_PENALTY)
                + 3 + 4, score());
    }

    private int score() {
        return (int)scores.getScore();
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        givenScores(0);

        // when
        kill();

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldStillZero_afterEatStone() {
        // given
        givenScores(0);

        // when
        eatStone();

        // then
        assertEquals(0, score());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);

        eatApple(3);

        assertEquals(3, score());

        // when
        scores.clear();

        // then
        assertEquals(0, score());
    }
}
