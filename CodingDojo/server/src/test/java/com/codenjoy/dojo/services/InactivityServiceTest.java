package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static com.codenjoy.dojo.services.incativity.InactivitySettings.Keys.INACTIVITY_ENABLED;
import static com.codenjoy.dojo.services.incativity.InactivitySettings.Keys.INACTIVITY_TIMEOUT;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.MULTIPLE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InactivityServiceTest extends AbstractPlayerGamesTest {

    InactivityService inactivity;

    @Before
    public void setUp() {
        super.setUp();
        playerGames = spy(playerGames);
        timeService = spy(timeService);
        inactivity = new InactivityService(playerGames, timeService);
    }

    void gavenPlayers(int timeout, long now) {
        createPlayer("player1", "room", "game", MULTIPLE)
                .setLastResponse(Long.MAX_VALUE);

        createPlayer("player2", "room", "game", MULTIPLE)
                .setLastResponse(minus(now, Calendar.SECOND, timeout + 10));

        createPlayer("player3", "room", "game", MULTIPLE)
                .setLastResponse(minus(now, Calendar.SECOND, timeout));

        createPlayer("player4", "room", "game", MULTIPLE)
                .setLastResponse(minus(now, Calendar.SECOND, timeout / 2));
    }

    long minus(long time, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(field, -1 * amount);
        return calendar.getTimeInMillis();
    }

    void setupInactivitySettings(int timeout, boolean enabled, boolean value) {
        if (playerGames.active().isEmpty()) {
            fail("There are no active players");
        }
        for (PlayerGame playerGame : playerGames.active()) {
            Settings settings = playerGame.getGameType().getSettings();

            when(settings.hasParameter(INACTIVITY_ENABLED.key()))
                    .thenReturn(enabled);
            when(settings.hasParameter(INACTIVITY_TIMEOUT.key()))
                    .thenReturn(enabled);

            when(settings.getParameter(INACTIVITY_ENABLED.key()))
                    .thenReturn(new CheckBox(INACTIVITY_ENABLED.key()).type(Boolean.class).update(value));
            when(settings.getParameter(INACTIVITY_TIMEOUT.key()))
                    .thenReturn(new EditBox(INACTIVITY_TIMEOUT.key()).type(Integer.class).update(timeout));
        }
    }

    @Test
    public void shouldRemovePlayers_afterInactivityTimeoutLimit() {
        // given
        int timeout = 5;
        long now = Calendar.getInstance().getTimeInMillis();
        when(timeService.now()).thenReturn(now);

        gavenPlayers(timeout, now);

        setupInactivitySettings(timeout, true, true);

        assertPlayers("[player1, player2, player3, player4]", playerGames.active());

        // when
        inactivity.tick();

        // then
        verify(playerGames, never()).removeCurrent(players.get(0));
        verify(playerGames).removeCurrent(players.get(1));
        verify(playerGames).removeCurrent(players.get(2));
        verify(playerGames, never()).removeCurrent(players.get(3));

        assertPlayers("[player1, player4]", playerGames.active());
    }

    @Test
    public void shouldNotRemovePlayers_afterInactivityTimeoutLimit_ifKickParameterAbsent() {
        //given
        int timeout = 5;
        long now = Calendar.getInstance().getTimeInMillis();
        when(timeService.now()).thenReturn(now);

        gavenPlayers(timeout, now);

        setupInactivitySettings(timeout, false, false);

        assertPlayers("[player1, player2, player3, player4]", playerGames.active());

        // when
        playerGames.tick();

        // then
        verify(playerGames, never()).removeCurrent(any(Player.class));

        assertPlayers("[player1, player2, player3, player4]", playerGames.active());
    }

    @Test
    public void shouldNotRemovePlayers_afterInactivityTimeoutLimit_ifKickParameterFalse() {
        // given
        int timeout = 5;
        long now = Calendar.getInstance().getTimeInMillis();
        when(timeService.now()).thenReturn(now);

        gavenPlayers(timeout, now);

        setupInactivitySettings(timeout, true, false);

        assertPlayers("[player1, player2, player3, player4]", playerGames.active());

        // when
        playerGames.tick();

        // then
        verify(playerGames, never()).removeCurrent(any(Player.class));

        assertPlayers("[player1, player2, player3, player4]", playerGames.active());
    }
}