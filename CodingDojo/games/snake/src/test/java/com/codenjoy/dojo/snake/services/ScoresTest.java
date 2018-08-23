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

    private Integer gameOverPenalty;
    private Integer startSnakeLength;
    private Integer eatStonePenalty;
    private Integer eatStoneDecrease;

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
        scores = new Scores(0, settings);

        gameOverPenalty = settings.getParameter("Game over penalty").type(Integer.class).getValue();
        startSnakeLength = settings.getParameter("Start snake length").type(Integer.class).getValue();
        eatStonePenalty = settings.getParameter("Eat stone penalty").type(Integer.class).getValue();
        eatStoneDecrease = settings.getParameter("Eat stone decrease").type(Integer.class).getValue();
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6

        snakeEatStone();  //-10

        snakeIsDead();    //-50

        assertEquals(140 + 3 + 4 + 5 + 6 - eatStonePenalty - gameOverPenalty,
                score());
    }

    @Test
    public void shouldSnakeLengthCantLessThen3() {
        scores = new Scores(0, settings);

        snakeEatStone();  //-10
        snakeEatStone();  //-10

        assertEquals(0, score());

        snakeEatApple();

        assertEquals(3, score());
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        scores = new Scores(0, settings);

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

        snakeEatStone();  //-10

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        assertEquals(3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12
                - eatStonePenalty + 3 + 4, score());
    }

    @Test
    public void shouldClearScoreTogetherWithSnakeLength() {
        scores = new Scores(0, settings);

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

        scores.clear();

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        assertEquals(3 + 4, score());
    }

    @Test
    public void shouldStartsFrom3AfterDead() {
        scores = new Scores(100, settings);

        snakeIsDead();    //-5

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        assertEquals(100 - gameOverPenalty + 3 + 4, score());
    }

    private int score() {
        return scores.getScore().intValue();
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new Scores(0, settings);

        snakeIsDead();    //-5

        assertEquals(0, score());
    }

    @Test
    public void shouldStillZeroAfterEatStone() {
        scores = new Scores(0, settings);

        snakeEatStone();    //-10

        assertEquals(0, score());
    }

    @Test
    public void shouldClearScore() {
        scores = new Scores(0, settings);

        snakeEatApple();  //+3

        scores.clear();

        assertEquals(0, score());
    }
}
