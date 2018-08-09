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


import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class StatisticsTest {

    private Statistics statistics;
    private Player player;
    private PlayerSpy spy;

    @Before
    public void setup() {
        statistics = new Statistics();

        player = mock(Player.class);
        spy = statistics.newPlayer(player);
    }

    @Test
    public void shouldActiveIfOneTickAfterRegister() {
        // given
        statistics.tick();

        // when
        List<Player> activePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 3);
        List<Player> notActivePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 3);

        // then
        assertEquals(1, activePlayers.size());
        assertSame(activePlayers.get(0), player);

        assertEquals(0, notActivePlayers.size());
    }

    @Test
    public void shouldActiveIfTwoTickAfterRegister() {
        // given
        statistics.tick();
        statistics.tick();

        // when then
        assertEquals(0, statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 2).size());
        assertEquals(1, statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 3).size());
        assertEquals(1, statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 4).size());

        assertEquals(1, statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 2).size());
        assertEquals(0, statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 3).size());
        assertEquals(0, statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 4).size());
    }

    @Test
    public void shouldStatisticsIfTwoTick() {
        // given
        statistics.tick();
        statistics.tick();

        // when
        List<Player> activePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 4);
        List<Player> notActivePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 2);

        // then
        assertEquals(1, activePlayers.size());
        assertSame(activePlayers.get(0), player);

        assertEquals(0, notActivePlayers.size());
    }

    @Test
    public void shouldNotActiveIfThreeTickAfterRegister() {
        // given
        statistics.tick();
        statistics.tick();
        statistics.tick();

        // when
        List<Player> activePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 3);
        List<Player> notActivePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 3);

        // then
        assertEquals(0, activePlayers.size());

        assertEquals(1, notActivePlayers.size());
        assertSame(notActivePlayers.get(0), player);
    }

    @Test
    public void shouldAgainActiveIfActCall() {
        // given
        statistics.tick();
        statistics.tick();
        statistics.tick();
        statistics.tick();
        statistics.tick();

        spy.act();

        // when
        List<Player> activePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 3);
        List<Player> notActivePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 3);

        // then
        assertEquals(1, activePlayers.size());
        assertSame(activePlayers.get(0), player);

        assertEquals(0, notActivePlayers.size());
    }

    @Test
    public void shouldAnotherUserActiveWhenFirstIsNot() {
        // given
        Player player2 = mock(Player.class);
        PlayerSpy spy2 = statistics.newPlayer(player2);

        statistics.tick();
        statistics.tick();
        statistics.tick();
        statistics.tick();
        statistics.tick();

        spy2.act();

        // when
        List<Player> activePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 3);
        List<Player> notActivePlayers = statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, 3);

        // then
        assertEquals(1, activePlayers.size());
        assertSame(activePlayers.get(0), player2);

        assertEquals(1, notActivePlayers.size());
        assertSame(notActivePlayers.get(0), player);
    }
}
