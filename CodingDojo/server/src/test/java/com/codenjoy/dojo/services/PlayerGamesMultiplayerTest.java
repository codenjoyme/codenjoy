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

import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PlayerGamesMultiplayerTest {

    private PlayerGames playerGames;
    private PrinterFactory printerFactory;

    private GameType single;
    private GameType multiple;
    private GameType tournament;
    private GameType triple;
    private GameType quadro;
    private GameType team4;

    private List<GameField> fields = new LinkedList<>();
    private List<GamePlayer> players = new LinkedList<>();

    @Before
    public void setup() {
        playerGames = new PlayerGames();
        printerFactory = mock(PrinterFactory.class);

        single = setupGameType("single", MultiplayerType.SINGLE);
        tournament = setupGameType("tournament", MultiplayerType.TOURNAMENT);
        triple = setupGameType("triple", MultiplayerType.TRIPLE);
        quadro = setupGameType("quadro", MultiplayerType.QUADRO);
        team4 = setupGameType("team4", MultiplayerType.TEAM.apply(4));
        multiple = setupGameType("multiple", MultiplayerType.MULTIPLE);
    }

    private GameType setupGameType(String gameName, MultiplayerType type) {
        GameType result = mock(GameType.class);
        when(result.getMultiplayerType()).thenReturn(type);
        when(result.name()).thenReturn(gameName);
        when(result.getPrinterFactory()).thenReturn(printerFactory);
        when(result.createGame()).thenAnswer(inv -> createGameField());
        when(result.createPlayer(any(EventListener.class), anyString(), anyString())).thenAnswer(inv -> createGamePlayer());
        return result;
    }

    private GameField createGameField() {
        GameField field = mock(GameField.class);
        when(field.reader()).thenReturn(mock(BoardReader.class));
        fields.add(field);
        return field;
    }

    private GamePlayer createGamePlayer() {
        GamePlayer player = mock(GamePlayer.class);
        when(player.isAlive()).thenReturn(true);
        players.add(player);
        return player;
    }

    @Test
    public void shouldEveryPlayerGoToTheirOwnField_whenSingle() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        // when
        PlayerGame playerGame1 = playerWantsToPlay(single, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(single, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(single, player3, null);

        // then
        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();

        assertGroup(field1)
                .notIn(field2)
                .notIn(field3)
                .check();

        // и каждый из них тикается независимо
        // when
        playerGames.tick();

        // then
        verify(field1, times(1)).quietTick();
        verify(field2, times(1)).quietTick();
        verify(field3, times(1)).quietTick();
    }

    private PlayerGame playerWantsToPlay(GameType gameType, Player player, Object data) {
        player.setGameType(gameType);
        return playerGames.add(player, null);
    }

    private static class GroupsAsserter {

        private List<List<GameField>> groups;

        public GroupsAsserter(GameField[] group) {
            groups = new LinkedList();
            notIn(group);
        }

        public GroupsAsserter notIn(GameField... anotherGroup) {
            groups.add(Arrays.asList(anotherGroup));
            return this;
        }

        public void check() {
            List<String> expected = new LinkedList<>();
            List<String> actual = new LinkedList<>();

            for (int i = 0; i < groups.size(); i++) {
                List<GameField> group = groups.get(i);
                for (int ii = 0; ii < group.size(); ii++) {
                    for (int jj = ii + 1; jj < group.size(); jj++) {
                        if (jj == ii) continue;
                        String message = String.format("[G%s:%s]==[G%s:%s]", i + 1, ii + 1, i + 1, jj + 1);
                        expected.add(message);
                        if (group.get(ii) == group.get(jj)) {
                            actual.add(message);
                        } else {
                            actual.add(message.replace("==", "!="));
                        }
                    }
                }
                for (int j = i + 1; j < groups.size(); j++) {
                    List<GameField> group1 = groups.get(i);
                    List<GameField> group2 = groups.get(j);
                    for (int ii = 0; ii < group1.size(); ii++) {
                        for (int jj = 0; jj < group2.size(); jj++) {
                            String message = String.format("[G%s:%s]!=[G%s:%s]", i + 1, ii + 1, j + 1, jj + 1);
                            expected.add(message);
                            if (group1.get(ii) != group2.get(jj)) {
                                actual.add(message);
                            } else {
                                actual.add(message.replace("!=", "=="));
                            }
                        }
                    }
                }
                assertEquals(expected.toString().replaceAll(",", ",\n"),
                        actual.toString().replaceAll(",", ",\n"));
            }
        }
    }

    private GroupsAsserter assertGroup(GameField... group1) {
        return new GroupsAsserter(group1);
    }

    @Test
    public void shouldTwoPlayerOnBoard_whenTournament() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        // when
        PlayerGame playerGame1 = playerWantsToPlay(tournament, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(tournament, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(tournament, player3, null);
        PlayerGame playerGame4 = playerWantsToPlay(tournament, player4, null);
        PlayerGame playerGame5 = playerWantsToPlay(tournament, player5, null);

        // then
        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertGroup(field1, field2)
                .notIn(field3, field4)
                .notIn(field5)
                .check();

        // и в группах борда тикается только раз
        // when
        playerGames.tick();

        // then
        verify(field1, times(1)).quietTick();
        verify(field3, times(1)).quietTick();
        verify(field5, times(1)).quietTick();
    }

    @Test
    public void shouldThreePlayerOnBoard_whenTriple() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        // when
        PlayerGame playerGame1 = playerWantsToPlay(triple, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(triple, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(triple, player3, null);
        PlayerGame playerGame4 = playerWantsToPlay(triple, player4, null);
        PlayerGame playerGame5 = playerWantsToPlay(triple, player5, null);

        // then
        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertGroup(field1, field2, field3)
                .notIn(field4, field5)
                .check();

        // и в группах борда тикается только раз
        // when
        playerGames.tick();

        // then
        verify(field1, times(1)).quietTick();
        verify(field4, times(1)).quietTick();
    }

    @Test
    public void shouldXPlayerOnBoard_whenTeam() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        // when
        PlayerGame playerGame1 = playerWantsToPlay(team4, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(team4, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(team4, player3, null);
        PlayerGame playerGame4 = playerWantsToPlay(team4, player4, null);
        PlayerGame playerGame5 = playerWantsToPlay(team4, player5, null);

        // then
        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertGroup(field1, field2, field3, field4)
                .notIn(field5)
                .check();

        // и в группах борда тикается только раз
        // when
        playerGames.tick();

        // then
        verify(field1, times(1)).quietTick();
        verify(field5, times(1)).quietTick();
    }

    @Test
    public void shouldFourPlayerOnBoard_whenQuadro() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();
        Player player4 = new Player();
        Player player5 = new Player();

        // when
        PlayerGame playerGame1 = playerWantsToPlay(quadro, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(quadro, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(quadro, player3, null);
        PlayerGame playerGame4 = playerWantsToPlay(quadro, player4, null);
        PlayerGame playerGame5 = playerWantsToPlay(quadro, player5, null);

        // then
        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertGroup(field1, field2, field3, field4)
                .notIn(field5)
                .check();

        // и в группах борда тикается только раз
        // when
        playerGames.tick();

        // then
        verify(field1, times(1)).quietTick();
        verify(field5, times(1)).quietTick();
    }

    @Test
    public void shouldAllPlayersGoToOneField_whenMultiple() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();
        Player player3 = new Player();

        // when
        PlayerGame playerGame1 = playerWantsToPlay(multiple, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(multiple, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(multiple, player3, null);

        // then
        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();

        assertGroup(field1, field2, field3)
                .check();

        // и в группах борда тикается только раз
        // when
        playerGames.tick();

        // then
        verify(field1, times(1)).quietTick();
    }

    @Test
    public void shouldPlayerStartsNewGame_whenSingle_whenRemove() {
        // given
        Player player1 = new Player();
        Player player2 = new Player();

        PlayerGame playerGame1 = playerWantsToPlay(single, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(single, player2, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();

        assertGroup(field1)
                .notIn(field2)
                .check();

        // when
        playerGame1 = playerWantsToPlay(single, player1, null);

        // then
        field1 = playerGame1.getGame().getField();

        assertGroup(field1)
                .notIn(field2)
                .check();

        // when
        playerGames.remove(player1);
        playerGame1 = playerWantsToPlay(single, player1, null);

        // then
        field1 = playerGame1.getGame().getField();

        assertGroup(field1)
                .notIn(field2)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTournament_whenRemove() {
        // given
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Player player3 = new Player("player3");

        PlayerGame playerGame1 = playerWantsToPlay(tournament, player1, null);
        PlayerGame playerGame2 = playerWantsToPlay(tournament, player2, null);
        PlayerGame playerGame3 = playerWantsToPlay(tournament, player3, null);

        GameField field1 = playerGame1.getGame().getField();
        GameField field2 = playerGame2.getGame().getField();
        GameField field3 = playerGame3.getGame().getField();

        assertGroup(field1, field2)
                .notIn(field3)
                .check();

        // when
        playerGames.remove(player1);

        // then
        field1 = playerGame1.getGame().getField();
        field2 = playerGame2.getGame().getField();
        field3 = playerGame3.getGame().getField();

        assertGroup(field2, field3)
                .check();
        assertEquals(null, field1);

        // when
        Player player4 = new Player("player4");
        Player player5 = new Player("player5");

        PlayerGame playerGame4 = playerWantsToPlay(tournament, player4, null);
        PlayerGame playerGame5 = playerWantsToPlay(tournament, player5, null);

        // then
        field1 = playerGame1.getGame().getField();
        field2 = playerGame2.getGame().getField();
        field3 = playerGame3.getGame().getField();
        GameField field4 = playerGame4.getGame().getField();
        GameField field5 = playerGame5.getGame().getField();

        assertGroup(field2, field3)
                .notIn(field4, field5)
                .check();
        assertEquals(null, field1);

        // when
        playerGames.remove(player2);

        // then
        field1 = playerGame1.getGame().getField();
        field2 = playerGame2.getGame().getField();
        field3 = playerGame3.getGame().getField();
        field4 = playerGame4.getGame().getField();
        field5 = playerGame5.getGame().getField();

        assertGroup(field4, field5)
                .notIn(field3)
                .check();
        assertEquals(null, field1);
        assertEquals(null, field2);

        // when
        Player player6 = new Player("player6");

        PlayerGame playerGame6 = playerWantsToPlay(tournament, player6, null);

        // then
        field1 = playerGame1.getGame().getField();
        field2 = playerGame2.getGame().getField();
        field3 = playerGame3.getGame().getField();
        field4 = playerGame4.getGame().getField();
        field5 = playerGame5.getGame().getField();
        GameField field6 = playerGame6.getGame().getField();

        assertGroup(field4, field5)
                .notIn(field3, field6)
                .check();
        assertEquals(null, field1);
        assertEquals(null, field2);
    }
}
