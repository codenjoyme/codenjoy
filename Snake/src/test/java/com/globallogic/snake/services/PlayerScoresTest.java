package com.globallogic.snake.services;

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
        PlayerScores scores = new PlayerScores(-10);

        scores.snakeEatApple();  //+10
        scores.snakeEatApple();  //+10

        scores.snakeEatStone();  //-100

        scores.snakeIsDead();    //-500

        assertEquals(- 10 + 10 + 10 - 100 - 500, scores.getScore());
    }
}
