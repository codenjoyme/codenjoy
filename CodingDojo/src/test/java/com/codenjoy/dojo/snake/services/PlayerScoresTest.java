package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.snake.model.middle.SnakeEvents;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:45 AM
 */
public class PlayerScoresTest {

    private PlayerScores scores;

    public void snakeEatApple() {
        scores.event(SnakeEvents.EAT_APPLE.name());
    }

    public void snakeIsDead() {
        scores.event(SnakeEvents.KILL.name());
    }

    public void snakeEatStone() {
        scores.event(SnakeEvents.EAT_STONE.name());
    }

    @Test
    public void shouldCollectScores() {
        scores = new PlayerScores(140);

        snakeEatApple();  //+3
        snakeEatApple();  //+4
        snakeEatApple();  //+5
        snakeEatApple();  //+6

        snakeEatStone();  //-10

        snakeIsDead();    //-50

        assertEquals(140 + 3 + 4 + 5 + 6 - PlayerScores.EAT_STONE_PENALTY - PlayerScores.GAME_OVER_PENALTY,
                scores.getScore());
    }

    @Test
    public void shouldShortLengthWhenEatStone() {
        scores = new PlayerScores(0);

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
                - PlayerScores.EAT_STONE_PENALTY + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStartsFrom3AfterDead() {
        scores = new PlayerScores(100);

        snakeIsDead();    //-5

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        assertEquals(100 - PlayerScores.GAME_OVER_PENALTY + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new PlayerScores(0);

        snakeIsDead();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterEatStone() {
        scores = new PlayerScores(0);

        snakeEatStone();    //-10

        assertEquals(0, scores.getScore());
    }
}
