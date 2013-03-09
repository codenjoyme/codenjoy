package com.codenjoy.dojo.snake.services;

import com.codenjoy.dojo.snake.model.GameLevel;
import com.codenjoy.dojo.snake.model.middle.SnakeEvents;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * User: oleksandr.baglai
 * Date: 11/13/12
 * Time: 2:06 AM
 */
public class InformationCollectorTest {

    private PlayerScores playerScores;
    private InformationCollector collector;

    @Before
    public void setup() {
        playerScores = mock(PlayerScores.class);
        collector = new InformationCollector(playerScores);
    }

    public void snakeEatApple() {
        collector.event(SnakeEvents.EAT_APPLE.name());
    }

    public void snakeIsDead() {
        collector.event(SnakeEvents.KILL.name());
    }

    public void snakeEatStone() {
        collector.event(SnakeEvents.EAT_STONE.name());
    }

    private void levelChanged(int levelNumber) {
        collector.levelChanged(levelNumber, null);
    }

    @Test
    public void shouldFIFOQueue() {
        when(playerScores.getScore()).thenReturn(0).thenReturn(1).thenReturn(0).thenReturn(2).thenReturn(0).thenReturn(3);

        snakeEatApple();
        snakeEatStone();
        snakeIsDead();
        levelChanged(4 - 1);

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldFIFOQueueButWhenPresentInformationAboutLevelChangedThenReturnItLast() {
        when(playerScores.getScore()).thenReturn(0).thenReturn(1).thenReturn(0).thenReturn(2).thenReturn(0).thenReturn(3);

        snakeEatApple();
        levelChanged(4 - 1);
        snakeEatStone();
        snakeIsDead();

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeEatApple() {
        snakeEatApple();

        verify(playerScores).event(SnakeEvents.EAT_APPLE.name());
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeEatStone() {
        snakeEatStone();

        verify(playerScores).event(SnakeEvents.EAT_STONE.name());
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeIsDead() {
        snakeIsDead();

        verify(playerScores).event(SnakeEvents.KILL.name());
    }

    @Test
    public void shouldCallPlayerScoreWhenLevelChanged() {
        GameLevel mock = mock(GameLevel.class);
        collector.levelChanged(14, mock);

        verify(playerScores).levelChanged(14, mock);
    }

    @Test
    public void shouldClearOldInfoWhenSetNew() {
        snakeEatApple();
        snakeEatApple();

        collector.setInfo("qwe");
        assertEquals("qwe", collector.getMessage());
    }

    @Test
    public void shouldIgnoreZeroPenalty() {
        when(playerScores.getScore()).thenReturn(0);

        snakeEatStone();
        snakeIsDead();

        assertEquals(null, collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldGetPenaltyToZero() {
        when(playerScores.getScore()).thenReturn(13).thenReturn(3).thenReturn(3).thenReturn(0);

        snakeEatStone();
        snakeIsDead();

        assertEquals("-10, -3", collector.getMessage());
        assertNull(collector.getMessage());
    }

}
