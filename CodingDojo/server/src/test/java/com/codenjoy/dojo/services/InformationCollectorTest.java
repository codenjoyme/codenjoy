package com.codenjoy.dojo.services;

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

    public enum Events {
        EAT_APPLE, KILL, EAT_STONE;
    }

    private PlayerScores playerScores;
    private InformationCollector collector;

    @Before
    public void setup() {
        playerScores = mock(PlayerScores.class);
        collector = new InformationCollector(playerScores);
    }

    public void snakeEatApple() {
        collector.event(Events.EAT_APPLE);
    }

    public void snakeIsDead() {
        collector.event(Events.KILL);
    }

    public void snakeEatStone() {
        collector.event(Events.EAT_STONE);
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

        verify(playerScores).event(Events.EAT_APPLE);
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeEatStone() {
        snakeEatStone();

        verify(playerScores).event(Events.EAT_STONE);
    }

    @Test
    public void shouldCallPlayerScoreWhenSnakeIsDead() {
        snakeIsDead();

        verify(playerScores).event(Events.KILL);
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
