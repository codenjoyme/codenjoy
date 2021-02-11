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


import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private Scores scores;
    private Settings settings;

    private SnakeSettings setup;

    public void snakeEatApple() {
        scores.event(Events.EAT_APPLE);
    }

    public void snakeIsDead() {
        scores.event(Events.KILL);
    }

    public void snakeEatStone() {
        scores.event(Events.EAT_STONE);
    }

    @Before
    public void setup() {
        settings = new SettingsImpl();
        setup = new SnakeSettings(settings);
        scores = getScores(0);
    }

    @Test
    public void shouldCollectScores() {
        // given
        scores = getScores(140);

        // when
        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6

        snakeEatStone();  //-10

        snakeIsDead();    //-50

        // then
        assertEquals(140 + 3 + 4 + 5 + 6
                        - setup.eatStonePenalty().getValue()
                        - setup.gameOverPenalty().getValue(),
                score());
        assertEquals(2, length());
    }

    @Test
    public void shouldSnakeLengthCantLessThen3() {
        // given
        scores = getScores(0);

        // when
        snakeEatStone();  //-10
        snakeEatStone();  //-10

        // then
        assertEquals(0, score());
        assertEquals(2, length());

        // when
        snakeEatApple();

        // then
        assertEquals(3, score());
        assertEquals(3, length());
    }

    private Scores getScores(int startScore) {
        return new Scores(startScore, setup);
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        // given
        scores = getScores(0);

        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6
        snakeEatApple();  //+7
        snakeEatApple();  //+8
        snakeEatApple();  //+9
        snakeEatApple();  //+10
        snakeEatApple();  //+11
        snakeEatApple();  //+12

        assertEquals(12, length());

        // when
        snakeEatStone();  //-10

        // then
        assertEquals(2, length());

        // when
        snakeEatApple();  //+3
        snakeEatApple();  //+4

        // then
        assertEquals(3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12
                - setup.eatStonePenalty().getValue()
                + 3 + 4, score());
        assertEquals(4, length());
    }

    @Test
    public void shouldClearScoreTogetherWithSnakeLength() {
        // given
        scores = getScores(0);

        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6
        snakeEatApple();  //+7
        snakeEatApple();  //+8
        snakeEatApple();  //+9
        snakeEatApple();  //+10
        snakeEatApple();  //+11
        snakeEatApple();  //+12

        assertEquals(12, length());

        // when
        scores.clear();

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        // then
        assertEquals(3 + 4, score());
        assertEquals(4, length());
    }

    @Test
    public void shouldStartsFrom3_afterDead() {
        // given
        scores = getScores(100);

        // when
        snakeIsDead();    //-5

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        // then
        assertEquals(100
                - setup.gameOverPenalty().getValue()
                + 3 + 4, score());
        assertEquals(4, length());
    }

    private int score() {
        return scores.getScore().intValue();
    }

    private int length() {
        return scores.getLength().intValue();
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        scores = getScores(0);

        // when
        snakeIsDead();    //-5

        // then
        assertEquals(0, score());
        assertEquals(2, length());
    }

    @Test
    public void shouldStillZero_afterEatStone() {
        // given
        scores = getScores(0);

        // when
        snakeEatStone();    //-10

        // then
        assertEquals(0, score());
        assertEquals(2, length());
    }

    @Test
    public void shouldClearScore() {
        // given
        scores = getScores(0);

        snakeEatApple();  //+3

        assertEquals(3, score());
        assertEquals(3, length());

        // when
        scores.clear();

        // then
        assertEquals(0, score());
        assertEquals(2, length());
    }
}
