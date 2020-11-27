package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import lombok.SneakyThrows;
import org.json.JSONObject;
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
        when(playerScores.getScore()).thenReturn(0);
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

    private void levelChanged(int level) {
        collector.levelChanged(new LevelProgress(level + 1, level, level - 1));
    }

    @Test
    public void shouldFIFOQueue() {
        when(playerScores.getScore())
                .thenReturn(0)
                .thenReturn(1)
                .thenReturn(0)
                .thenReturn(2)
                .thenReturn(0)
                .thenReturn(3);

        snakeEatApple();
        snakeEatStone();
        snakeIsDead();
        levelChanged(4);

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    @Test
    public void shouldFIFOQueue_ifJsonObject() {
        when(playerScores.getScore())
                .thenReturn(json(0))
                .thenReturn(json(1))
                .thenReturn(json(0))
                .thenReturn(json(2))
                .thenReturn(json(0))
                .thenReturn(json(3));

        snakeEatApple();
        snakeEatStone();
        snakeIsDead();
        levelChanged(4);

        assertEquals("+1, +2, +3, Level 4", collector.getMessage());
        assertNull(collector.getMessage());
    }

    private JSONObject json(int score) {
        return new JSONObject(String.format("{'score':%s}", score));
    }

    @Test
    public void shouldFIFOQueueButWhenPresentInformationAboutLevelChangedThenReturnItLast() {
        when(playerScores.getScore())
                .thenReturn(0)
                .thenReturn(1)
                .thenReturn(0)
                .thenReturn(2)
                .thenReturn(0)
                .thenReturn(3);

        snakeEatApple();
        levelChanged(4);
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

    @Test
    public void shouldPrintCustomMessage() {
        collector.event(new CustomMessage("3"));
        collector.event(new CustomMessage("2"));
        collector.event(new CustomMessage("1"));
        collector.event(new CustomMessage("Fight!!!"));

        assertEquals("3, 2, 1, Fight!!!", collector.getMessage());
        assertNull(collector.getMessage());
    }

}
