package com.globallogic.snake.services;

import com.globallogic.snake.model.GameLevel;
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

    @Test
    public void shouldFIFOQueue() {
        when(playerScores.getScore()).thenReturn(0).thenReturn(1).thenReturn(0).thenReturn(2).thenReturn(0).thenReturn(3);

        collector.snakeEatApple();
        collector.snakeEatStone();
        collector.snakeIsDead();
        collector.levelChanged(4 - 1, null);

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldFIFOQueueButWhenPresentInformationAboutLevelChangedThenReturnItLast() {
        when(playerScores.getScore()).thenReturn(0).thenReturn(1).thenReturn(0).thenReturn(2).thenReturn(0).thenReturn(3);

        collector.snakeEatApple();
        collector.levelChanged(4 - 1, null);
        collector.snakeEatStone();
        collector.snakeIsDead();

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeEatApple() {
        collector.snakeEatApple();

        verify(playerScores).snakeEatApple();
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeEatStone() {
        collector.snakeEatStone();

        verify(playerScores).snakeEatStone();
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeIsDead() {
        collector.snakeIsDead();

        verify(playerScores).snakeIsDead();
    }

    @Test
    public void shouldCallPlayerScoreWhenLevelChanged() {
        GameLevel mock = mock(GameLevel.class);
        collector.levelChanged(14, mock);

        verify(playerScores).levelChanged(14, mock);
    }

    @Test
    public void shouldClearOldInfoWhenSetNew() {
        collector.snakeEatApple();
        collector.snakeEatApple();

        collector.setInfo("qwe");
        assertEquals("qwe", collector.getMessage());
    }


}
