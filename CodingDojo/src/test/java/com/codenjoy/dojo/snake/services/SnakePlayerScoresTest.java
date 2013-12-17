package com.codenjoy.dojo.snake.services;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:45 AM
 */
public class SnakePlayerScoresTest {

    private SnakePlayerScores scores;

    public void snakeEatApple() {
        scores.event(SnakeEvents.EAT_APPLE);
    }

    public void snakeIsDead() {
        scores.event(SnakeEvents.KILL);
    }

    public void snakeEatStone() {
        scores.event(SnakeEvents.EAT_STONE);
    }

    @Test
    public void shouldCollectScores() {
        scores = new SnakePlayerScores(140);

        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6

        snakeEatStone();  //-10

        snakeIsDead();    //-50

        assertEquals(140 + 3 + 4 + 5 + 6 - SnakePlayerScores.EAT_STONE_PENALTY - SnakePlayerScores.GAME_OVER_PENALTY,
                scores.getScore());
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        scores = new SnakePlayerScores(0);

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
                - SnakePlayerScores.EAT_STONE_PENALTY + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStartsFrom3AfterDead() {
        scores = new SnakePlayerScores(100);

        snakeIsDead();    //-5

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        assertEquals(100 - SnakePlayerScores.GAME_OVER_PENALTY + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new SnakePlayerScores(0);

        snakeIsDead();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterEatStone() {
        scores = new SnakePlayerScores(0);

        snakeEatStone();    //-10

        assertEquals(0, scores.getScore());
    }
}
