package com.codenjoy.dojo.services;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;

/**
 * Created by Sanja on 15.02.14.
 */
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
