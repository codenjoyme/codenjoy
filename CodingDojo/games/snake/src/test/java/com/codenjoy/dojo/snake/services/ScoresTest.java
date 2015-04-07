package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:45 AM
 */
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
                scores.getScore());
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
                - eatStonePenalty + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStartsFrom3AfterDead() {
        scores = new Scores(100, settings);

        snakeIsDead();    //-5

        snakeEatApple();  //+3
        snakeEatApple();  //+4

        assertEquals(100 - gameOverPenalty + 3 + 4, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        scores = new Scores(0, settings);

        snakeIsDead();    //-5

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterEatStone() {
        scores = new Scores(0, settings);

        snakeEatStone();    //-10

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        scores = new Scores(0, settings);

        snakeEatApple();  //+3

        scores.clear();

        assertEquals(0, scores.getScore());
    }
}
