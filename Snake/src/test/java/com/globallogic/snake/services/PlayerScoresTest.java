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

        scores.snakeEatApple();  //+2
        scores.snakeEatApple();  //+3
        scores.snakeEatApple();  //+4
        scores.snakeEatApple();  //+5

        scores.snakeEatStone();  //-10

        scores.snakeIsDead();    //-50

        assertEquals(-10 + 2 + 3 + 4 + 5 - 10 - 50, scores.getScore());
    }
}
