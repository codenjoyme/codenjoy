package com.codenjoy.dojo.snake.services;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:45 AM
 */
public class PlayerScoresTest {

    @Test
    public void shouldCollectScores() {
        PlayerScores scores = new PlayerScores(140);

        scores.snakeEatApple();  //+3
        scores.snakeEatApple();  //+4
        scores.snakeEatApple();  //+5
        scores.snakeEatApple();  //+6

        scores.snakeEatStone();  //-10

        scores.snakeIsDead();    //-50

        assertEquals(140 + 3 + 4 + 5 + 6 - PlayerScores.EAT_STONE_PENALTY - PlayerScores.GAME_OVER_PENALTY,
                scores.getScore());
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        PlayerScores scores = new PlayerScores(0);

        scores.snakeEatApple();  //+3
        scores.snakeEatApple();  //+4
        scores.snakeEatApple();  //+5
        scores.snakeEatApple();  //+6
        scores.snakeEatApple();  //+7
        scores.snakeEatApple();  //+8
        scores.snakeEatApple();  //+9
        scores.snakeEatApple();  //+10
        scores.snakeEatApple();  //+11
        scores.snakeEatApple();  //+12

        scores.snakeEatStone();  //-10

        scores.snakeEatApple();  //+3
        scores.snakeEatApple();  //+4

        assertEquals(3 + 4 + 5 + 6 + 7 + 8 + 9 + 10 + 11 + 12
                - PlayerScores.EAT_STONE_PENALTY + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStartsFrom3AfterDead() {
        PlayerScores scores = new PlayerScores(100);

        scores.snakeIsDead();    //-5

        scores.snakeEatApple();  //+3
        scores.snakeEatApple();  //+4

        assertEquals(100 - PlayerScores.GAME_OVER_PENALTY + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        PlayerScores scores = new PlayerScores(0);

        scores.snakeIsDead();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterEatStone() {
        PlayerScores scores = new PlayerScores(0);

        scores.snakeEatStone();    //-10

        assertEquals(0, scores.getScore());
    }
}
