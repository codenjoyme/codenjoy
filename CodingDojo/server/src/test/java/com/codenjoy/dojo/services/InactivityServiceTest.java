package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.multiplayer.Sweeper;
import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static com.codenjoy.dojo.services.incativity.InactivitySettings.Keys.INACTIVITY_ENABLED;
import static com.codenjoy.dojo.services.incativity.InactivitySettings.Keys.INACTIVITY_TIMEOUT;
import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.MULTIPLE;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class InactivityServiceTest extends AbstractDealsTest {

    private InactivityService inactivity;

    @Before
    public void setup() {
        super.setup();
        deals = spy(deals);
        timeService = spy(timeService);
        inactivity = new InactivityService(deals, timeService);
    }

    private void gavenPlayers(int timeout, long now) {
        createPlayer("player1", "room", "game", MULTIPLE)
                .setLastResponse(Long.MAX_VALUE);

        createPlayer("player2", "room", "game", MULTIPLE)
                .setLastResponse(minus(now, Calendar.SECOND, timeout + 10));

        createPlayer("player3", "room", "game", MULTIPLE)
                .setLastResponse(minus(now, Calendar.SECOND, timeout));

        createPlayer("player4", "room", "game", MULTIPLE)
                .setLastResponse(minus(now, Calendar.SECOND, timeout / 2));
    }

    private long minus(long time, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(field, -1 * amount);
        return calendar.getTimeInMillis();
    }

    private void setupInactivitySettings(int timeout, boolean enabled, boolean value) {
        if (deals.active().isEmpty()) {
            fail("There are no active players");
        }
        for (Deal deal : deals.active()) {
            Settings settings = deal.getGameType().getSettings();

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

        assertPlayers("[player1, player2, player3, player4]", deals.active());

        // when
        inactivity.tick();

        // then
        verify(deals, never()).remove(eq(players.get(0).getId()), any(Sweeper.class));
        verify(deals).remove(eq(players.get(1).getId()), any(Sweeper.class));
        verify(deals).remove(eq(players.get(2).getId()), any(Sweeper.class));
        verify(deals, never()).remove(eq(players.get(3).getId()), any(Sweeper.class));

        assertPlayers("[player1, player4]", deals.active());
    }

    @Test
    public void shouldNotRemovePlayers_afterInactivityTimeoutLimit_ifKickParameterAbsent() {
        //given
        int timeout = 5;
        long now = Calendar.getInstance().getTimeInMillis();
        when(timeService.now()).thenReturn(now);

        gavenPlayers(timeout, now);

        setupInactivitySettings(timeout, false, false);

        assertPlayers("[player1, player2, player3, player4]", deals.active());

        // when
        deals.tick();

        // then
        verify(deals, never()).remove(any(String.class), any(Sweeper.class));

        assertPlayers("[player1, player2, player3, player4]", deals.active());
    }

    @Test
    public void shouldNotRemovePlayers_afterInactivityTimeoutLimit_ifKickParameterFalse() {
        // given
        int timeout = 5;
        long now = Calendar.getInstance().getTimeInMillis();
        when(timeService.now()).thenReturn(now);

        gavenPlayers(timeout, now);

        setupInactivitySettings(timeout, true, false);

        assertPlayers("[player1, player2, player3, player4]", deals.active());

        // when
        deals.tick();

        // then
        verify(deals, never()).remove(any(String.class), any(Sweeper.class));

        assertPlayers("[player1, player2, player3, player4]", deals.active());
    }
}
