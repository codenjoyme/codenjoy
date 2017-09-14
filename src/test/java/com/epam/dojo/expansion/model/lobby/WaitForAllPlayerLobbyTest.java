package com.epam.dojo.expansion.model.lobby;

import com.codenjoy.dojo.services.EventListener;
import com.epam.dojo.expansion.model.GameFactory;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.PlayerBoard;
import com.epam.dojo.expansion.model.ProgressBar;
import com.epam.dojo.expansion.services.SettingsWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.exceptions.verification.VerificationInOrderFailure;

import java.util.LinkedList;
import java.util.List;

import static com.epam.dojo.expansion.model.AbstractSinglePlayersTest.*;
import static com.epam.dojo.expansion.services.SettingsWrapper.data;
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

        gotoLobby(PLAYER1);
        gotoLobby(PLAYER2);
        gotoLobby(PLAYER3);
        gotoLobby(PLAYER4);
        gotoLobby(PLAYER5); // wait on lobby
        gotoLobby(PLAYER6); // wait on lobby
        gotoLobby(PLAYER7); // wait on lobby

        when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

        // when
        lobby.tick();

        // then
        InOrder inOrder = Mockito.inOrder(progressBars.toArray());
        inOrder.verify(progress(PLAYER1)).setCurrent(room);
        inOrder.verify(progress(PLAYER2)).setCurrent(room);
        inOrder.verify(progress(PLAYER3)).setCurrent(room);
        inOrder.verify(progress(PLAYER4)).setCurrent(room);

        verifyNoMoreInteractions(progress(PLAYER5));
        verifyNoMoreInteractions(progress(PLAYER6));
        verifyNoMoreInteractions(progress(PLAYER7));
    }

    @Test
    public void shouldOnSecondAttemptIfPlayersEnoughFirstlyAddedPlayersThatWaitingOneGameThenNewPlayers() {
        // given
        shouldSomebodyStayInLobbyIfPlayersIsNotEnough();

        when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

        gotoLobby(PLAYER1);
        gotoLobby(PLAYER2);
        gotoLobby(PLAYER3);
        gotoLobby(PLAYER4);

        // when
        lobby.tick();

        // then
        InOrder inOrder = Mockito.inOrder(progressBars.toArray());
        inOrder.verify(progress(PLAYER5)).setCurrent(room);
        inOrder.verify(progress(PLAYER6)).setCurrent(room);
        inOrder.verify(progress(PLAYER7)).setCurrent(room);
        inOrder.verify(progress(PLAYER1)).setCurrent(room);

        verifyNoMoreInteractions(progress(PLAYER2));
        verifyNoMoreInteractions(progress(PLAYER3));
        verifyNoMoreInteractions(progress(PLAYER4));
    }

    @Test
    public void shouldOnSecondAttemptIfPlayersEnoughFirstlyAddedPlayersThatWaitingOneGameThenNewPlayers_whenRandomMixed_onlyNewUsers() {
        // given
        shouldSomebodyStayInLobbyIfPlayersIsNotEnough();

        boolean old = data.shufflePlayers();
        try {
            data.shufflePlayers(true);

            when(room.isFree()).thenReturn(true, true, true, true, false, true, true, true);

            gotoLobby(PLAYER1);
            gotoLobby(PLAYER2);
            gotoLobby(PLAYER3);
            gotoLobby(PLAYER4);

            // when
            lobby.tick();

            // then
            InOrder inOrder = Mockito.inOrder(progressBars.toArray());
            inOrder.verify(progress(PLAYER5)).setCurrent(room);
            inOrder.verify(progress(PLAYER6)).setCurrent(room);
            inOrder.verify(progress(PLAYER7)).setCurrent(room);
            // one more player but we don't know which one
            // inOrder.verify(progress(PLAYERX)).setCurrent(room);

            int count = 0;
            try {
                verifyNoMoreInteractions(progress(PLAYER1));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER2));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER3));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER4));
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

            gotoLobby(PLAYER5);
            gotoLobby(PLAYER6);
            gotoLobby(PLAYER7);
            gotoLobby(PLAYER1);

            // when
            lobby.tick();

            // then
            InOrder inOrder = Mockito.inOrder(progressBars.toArray());
            inOrder.verify(progress(PLAYER2)).setCurrent(room);
            inOrder.verify(progress(PLAYER3)).setCurrent(room);
            inOrder.verify(progress(PLAYER4)).setCurrent(room);
            // one more player but we don't know which one
            // inOrder.verify(progress(PLAYERX)).setCurrent(room);

            int count = 0;
            try {
                verifyNoMoreInteractions(progress(PLAYER5));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER6));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER7));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER1));
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

            gotoLobby(PLAYER5);
            gotoLobby(PLAYER6);
            gotoLobby(PLAYER7);
            gotoLobby(PLAYER1);

            // when
            lobby.remove(player(PLAYER2)); // removed
            players.add(player(PLAYER2)); // then added
            lobby.tick();

            // then
            InOrder inOrder = Mockito.inOrder(progressBars.toArray());
            // just removed
            // inOrder.verify(progress(PLAYER2)).setCurrent(room);
            inOrder.verify(progress(PLAYER3)).setCurrent(room);
            inOrder.verify(progress(PLAYER4)).setCurrent(room);
            // two more player but we don't know which
            // inOrder.verify(progress(PLAYERX)).setCurrent(room);
            // inOrder.verify(progress(PLAYERY)).setCurrent(room);

            int count = 0;
            try {
                verifyNoMoreInteractions(progress(PLAYER5));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER6));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER7));
                count++;
            } catch (NoInteractionsWanted e) {
                // do nothing
            }
            try {
                verifyNoMoreInteractions(progress(PLAYER1));
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
        verifyNoMoreInteractions(progress(PLAYER5));
        verifyNoMoreInteractions(progress(PLAYER6));
        verifyNoMoreInteractions(progress(PLAYER7));
        verifyNoMoreInteractions(progress(PLAYER1));
        verifyNoMoreInteractions(progress(PLAYER2));
        verifyNoMoreInteractions(progress(PLAYER3));
        verifyNoMoreInteractions(progress(PLAYER4));
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
        Player player = new Player(mock(EventListener.class), progressBar);
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
