package com.codenjoy.dojo.expansion.model.lobby;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.expansion.model.*;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.expansion.model.GameFactory;
import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.PlayerBoard;
import com.codenjoy.dojo.expansion.model.ProgressBar;
import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.NoInteractionsWanted;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Oleksandr_Baglai on 2017-09-14.
 */
public class WaitForAllPlayerLobbyTest {

    private List<ProgressBar> progressBars;
    private GameFactory factory;
    private WaitForAllPlayerLobby lobby;
    private List<Player> players;
    private PlayerBoard room;

    @Before
    public void setup() {
        SettingsWrapper.setup()
                .lobbyCapacity(4)
                .shufflePlayers(false);

        factory = mock(GameFactory.class);
        room = mock(PlayerBoard.class);
        when(factory.newMultiple()).thenReturn(room);
        lobby = new WaitForAllPlayerLobby(factory);

        progressBars = new LinkedList<>();
        players = new LinkedList<>();
    }

    @Test
    public void shouldSomebodyStayInLobbyIfPlayersIsNotEnough() {
        // given
        createPlayer();
        createPlayer();
        createPlayer();
        createPlayer();
        createPlayer();
        createPlayer();
        createPlayer();

        gotoLobby(AbstractSinglePlayersTest.PLAYER1);
        gotoLobby(AbstractSinglePlayersTest.PLAYER2);
        gotoLobby(AbstractSinglePlayersTest.PLAYER3);
        gotoLobby(AbstractSinglePlayersTest.PLAYER4);
        gotoLobby(AbstractSinglePlayersTest.PLAYER5); // wait on lobby
        gotoLobby(AbstractSinglePlayersTest.PLAYER6); // wait on lobby
        gotoLobby(AbstractSinglePlayersTest.PLAYER7); // wait on lobby

        when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

        // when
        lobby.tick();

        // then
        InOrder inOrder = Mockito.inOrder(progressBars.toArray());
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER1)).setCurrent(room);
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER2)).setCurrent(room);
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER3)).setCurrent(room);
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER4)).setCurrent(room);

        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER5));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER6));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER7));
    }

    @Test
    public void shouldOnSecondAttemptIfPlayersEnoughFirstlyAddedPlayersThatWaitingOneGameThenNewPlayers() {
        // given
        shouldSomebodyStayInLobbyIfPlayersIsNotEnough();

        when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

        gotoLobby(AbstractSinglePlayersTest.PLAYER1);
        gotoLobby(AbstractSinglePlayersTest.PLAYER2);
        gotoLobby(AbstractSinglePlayersTest.PLAYER3);
        gotoLobby(AbstractSinglePlayersTest.PLAYER4);

        // when
        lobby.tick();

        // then
        InOrder inOrder = Mockito.inOrder(progressBars.toArray());
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER5)).setCurrent(room);
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER6)).setCurrent(room);
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER7)).setCurrent(room);
        inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER1)).setCurrent(room);

        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER2));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER3));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER4));
    }

    @Test
    public void shouldOnSecondAttemptIfPlayersEnoughFirstlyAddedPlayersThatWaitingOneGameThenNewPlayers_whenRandomMixed_onlyNewUsers() {
        // given
        shouldSomebodyStayInLobbyIfPlayersIsNotEnough();

        boolean old = data.shufflePlayers();
        try {
            data.shufflePlayers(true);

            when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

            gotoLobby(AbstractSinglePlayersTest.PLAYER1);
            gotoLobby(AbstractSinglePlayersTest.PLAYER2);
            gotoLobby(AbstractSinglePlayersTest.PLAYER3);
            gotoLobby(AbstractSinglePlayersTest.PLAYER4);

            // when
            lobby.tick();

            // then
            InOrder inOrder = Mockito.inOrder(progressBars.toArray());
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER5)).setCurrent(room);
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER6)).setCurrent(room);
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER7)).setCurrent(room);
            // one more player but we don't know which one
            // inOrder.verify(progress(PLAYERX)).setCurrent(room);

            int count = 0;
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER1));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER2));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER3));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER4));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            assertEquals(3, count);
        } finally {
            data.shufflePlayers(old);
        }
    }

    @Test
    public void shouldWaitALotListMustBeCleaned() {
        // given
        shouldOnSecondAttemptIfPlayersEnoughFirstlyAddedPlayersThatWaitingOneGameThenNewPlayers();

        boolean old = data.shufflePlayers();
        try {
            data.shufflePlayers(true);

            when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

            gotoLobby(AbstractSinglePlayersTest.PLAYER5);
            gotoLobby(AbstractSinglePlayersTest.PLAYER6);
            gotoLobby(AbstractSinglePlayersTest.PLAYER7);
            gotoLobby(AbstractSinglePlayersTest.PLAYER1);

            // when
            lobby.tick();

            // then
            InOrder inOrder = Mockito.inOrder(progressBars.toArray());
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER2)).setCurrent(room);
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER3)).setCurrent(room);
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER4)).setCurrent(room);
            // one more player but we don't know which one
            // inOrder.verify(progress(PLAYERX)).setCurrent(room);

            int count = 0;
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER5));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER6));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER7));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER1));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            assertEquals(3, count);
        } finally {
            data.shufflePlayers(old);
        }
    }

    @Test
    public void shouldRemovePlayerAlsoFromWaitALotList() {
        // given
        shouldOnSecondAttemptIfPlayersEnoughFirstlyAddedPlayersThatWaitingOneGameThenNewPlayers();

        boolean old = data.shufflePlayers();
        try {
            data.shufflePlayers(true);

            when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

            gotoLobby(AbstractSinglePlayersTest.PLAYER5);
            gotoLobby(AbstractSinglePlayersTest.PLAYER6);
            gotoLobby(AbstractSinglePlayersTest.PLAYER7);
            gotoLobby(AbstractSinglePlayersTest.PLAYER1);

            // when
            lobby.remove(player(AbstractSinglePlayersTest.PLAYER2)); // removed
            players.add(player(AbstractSinglePlayersTest.PLAYER2)); // then added
            lobby.tick();

            // then
            InOrder inOrder = Mockito.inOrder(progressBars.toArray());
            // just removed
            // inOrder.verify(progress(PLAYER2)).setCurrent(room);
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER3)).setCurrent(room);
            inOrder.verify(progress(AbstractSinglePlayersTest.PLAYER4)).setCurrent(room);
            // two more player but we don't know which
            // inOrder.verify(progress(PLAYERX)).setCurrent(room);
            // inOrder.verify(progress(PLAYERY)).setCurrent(room);

            int count = 0;
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER5));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER6));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER7));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER1));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            assertEquals(2, count);
        } finally {
            data.shufflePlayers(old);
        }
    }

    @Test
    public void shouldWaitALotButStillNotEnough() {
        // given
        shouldSomebodyStayInLobbyIfPlayersIsNotEnough();

        when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

        // when
        lobby.tick();

        // then
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER5));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER6));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER7));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER1));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER2));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER3));
        verifyNoMoreInteractions(progress(AbstractSinglePlayersTest.PLAYER4));
    }

    private ProgressBar progress(int player) {
        return progressBars.get(player);
    }

    private void gotoLobby(int player) {
        lobby.start(player(player));
    }

    private Player player(int player) {
        return players.get(player);
    }

    private Player createPlayer() {
        ProgressBar progressBar = createProgressBar();
        Player player = new Player(mock(EventListener.class), progressBar, null);
        reset(progressBar);
        players.add(player);
        lobby.addPlayer(player);
        return player;
    }

    private ProgressBar createProgressBar() {
        ProgressBar result = mock(ProgressBar.class);
        progressBars.add(result);
        return result;
    }
}
