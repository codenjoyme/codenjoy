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
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

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
    private List<GamePlayer> gamePlayers = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<Supplier<GameField>> filedSuppliers = new LinkedList<>();;

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
        gamePlayers.add(player);
        return player;
    }

    private void verifyAllFieldsTicks(VerificationMode count) {
        filedSuppliers.forEach(s -> verify(s.get(), count).quietTick());
    }

    private void playerWantsToPlay(GameType gameType) {
        int index = gamePlayers.size();
        Player player = new Player("player" + index);
        players.add(player);
        PlayerGame playerGame = playerWantsToPlay(gameType, player, null);
        filedSuppliers.add(() -> playerGame.getGame().getField());
    }

    private PlayerGame playerWantsToPlay(GameType gameType, Player player, Object data) {
        player.setGameType(gameType);
        return playerGames.add(player, null);
    }

    private static class GroupsAsserter {
        private Function<Integer, GameField> mapper;
        private List<List<Integer>> groups;
        private List<Integer> isNull;

        public GroupsAsserter(Function<Integer, GameField> mapper) {
            groups = new LinkedList();
            isNull = new LinkedList();
            this.mapper = mapper;
        }

        public GroupsAsserter notIn(Integer... anotherGroup) {
            groups.add(Arrays.asList(anotherGroup));
            return this;
        }

        public void check() {
            List<String> expected = new LinkedList<>();
            List<String> actual = new LinkedList<>();

            for (int i = 0; i < groups.size(); i++) {
                List<Integer> group = groups.get(i);
                for (int ii = 0; ii < group.size(); ii++) {
                    for (int jj = ii + 1; jj < group.size(); jj++) {
                        if (jj == ii) continue;
                        Integer fj = group.get(jj);
                        Integer fi = group.get(ii);
                        String message = String.format("field[%s] == field[%s]", fi, fj);
                        expected.add(message);
                        if (mapper.apply(fi) == mapper.apply(fj)) {
                            actual.add(message);
                        } else {
                            actual.add(message.replace("==", "!="));
                        }
                    }
                }
                for (int j = i + 1; j < groups.size(); j++) {
                    List<Integer> group1 = groups.get(i);
                    List<Integer> group2 = groups.get(j);
                    for (int ii = 0; ii < group1.size(); ii++) {
                        for (int jj = 0; jj < group2.size(); jj++) {
                            Integer fi = group1.get(ii);
                            Integer fj = group2.get(jj);
                            String message = String.format("field[%s] != field[%s]", fi, fj);
                            expected.add(message);
                            if (mapper.apply(fi) != mapper.apply(fj)) {
                                actual.add(message);
                            } else {
                                actual.add(message.replace("!=", "=="));
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < isNull.size(); i++) {
                String message = String.format("field[%s] %s null", i, (mapper.apply(isNull.get(i)) == null)?"==":"!=");
                actual.add(message);
                expected.add(message.replace("!=", "=="));
            }
            assertEquals(expected.toString().replaceAll(",", ",\n"),
                    actual.toString().replaceAll(",", ",\n"));
        }

        public GroupsAsserter isNull(Integer... fields) {
            isNull.addAll(Arrays.asList(fields));
            return this;
        }
    }

    private GroupsAsserter assertGroup(Integer... group) {
        return new GroupsAsserter(index -> filedSuppliers.get(index).get())
                .notIn(group);
    }

    @Test
    public void shouldEveryPlayerGoToTheirOwnField_whenSingle() {
        // given
        playerWantsToPlay(single);
        playerWantsToPlay(single);
        playerWantsToPlay(single);

        // then
        assertGroup(0)
                .notIn(1)
                .notIn(2)
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    } 
    
    @Test
    public void shouldTwoPlayerOnBoard_whenTournament() {
        // given
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        assertGroup(0, 1)
                .notIn(2, 3)
                .notIn(4)
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldThreePlayerOnBoard_whenTriple() {
        // given
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);
        playerWantsToPlay(triple);

        assertGroup(0, 1, 2)
                .notIn(3, 4)
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldXPlayerOnBoard_whenTeam() {
        // given
        playerWantsToPlay(team4);
        playerWantsToPlay(team4);
        playerWantsToPlay(team4);
        playerWantsToPlay(team4);
        playerWantsToPlay(team4);

        assertGroup(0, 1, 2, 3)
                .notIn(4)
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldFourPlayerOnBoard_whenQuadro() {
        // given
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);
        playerWantsToPlay(quadro);

        assertGroup(0, 1, 2, 3)
                .notIn(4)
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldAllPlayersGoToOneField_whenMultiple() {
        // given
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);
        playerWantsToPlay(multiple);

        assertGroup(0, 1, 2)
                .check();

        // when
        playerGames.tick();

        // then
        verifyAllFieldsTicks(times(1));
    }

    @Test
    public void shouldPlayerStartsNewGame_whenSingle_whenRemove() {
        // given
        playerWantsToPlay(single);
        playerWantsToPlay(single);

        assertGroup(0)
                .notIn(1)
                .check();

        // when
        playerWantsToPlay(single);

        assertGroup(0)
                .notIn(1)
                .notIn(2)
                .check();

        // when
        playerGames.remove(players.get(0));

        // then
        assertGroup(0)
                .notIn(1)
                .check();
    }

    @Test
    public void shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTournament_whenRemove() {
        // given
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        assertGroup(0, 1)
                .notIn(2)
                .check();

        // when
        playerGames.remove(players.get(0));

        // then
        assertGroup(1, 2)
                .isNull(0)
                .check();

        // when
        playerWantsToPlay(tournament);
        playerWantsToPlay(tournament);

        // then
        assertGroup(1, 2)
                .notIn(3, 4)
                .isNull(0)
                .check();

        // when
        playerGames.remove(players.get(1));

        // then
        assertGroup(3, 4)
                .notIn(2)
                .isNull(0, 1)
                .check();

        // when
        playerWantsToPlay(tournament);

        // then
        assertGroup(3, 4)
                .notIn(2, 5)
                .isNull(0, 1)
                .check();
    }

    // TODO shouldPlayerStartsNewGameAndAnotherGoWithHim_whenTournament_whenRemove сделать такой же тест для других типов комнат
}
