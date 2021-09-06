package com.codenjoy.dojo.services.multiplayer;

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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.FieldService;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.google.common.collect.Iterators;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_TEAMS_PER_ROOM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class SpreaderTest {

    private final Spreader spreader = new Spreader(){{
        fields = SpreaderTest.this.fieldService = mock(FieldService.class);
    }};
    private FieldService fieldService;
    private final String room = "room";
    private final MultiplayerType multiplayerType = MultiplayerType.MULTIPLE;
    private final Supplier<GameField> getField = () -> newField();
    private List<GameField> fields = new LinkedList<>();
    private List<Player> players = new LinkedList<>();

    private Player newPlayer() {
        Player result = new Player("player" + players.size());
        players.add(result);
        return result;
    }

    private GameField newField() {
        GameField field = mock(GameField.class);
        fields.add(field);
        return field;
    }

    private Deal newDeal(int teamId, SettingsReader settings) {
        GamePlayer gamePlayer = new GamePlayer(event -> {}, settings) {};
        Game game = new Single(gamePlayer, null);
        Deal result = new Deal(newPlayer(), game, room);
        result.setTeamId(teamId);
        return result;
    }

    @Test
    public void fieldFor() {
        boolean failed = false;
        for (int playersCount = 2; playersCount <= 50; playersCount++) {
            for (int playersPerRoom = 2; playersPerRoom <= playersCount; playersPerRoom++) {
                for (int teamsPerRoom = 1; teamsPerRoom <= playersPerRoom; teamsPerRoom++) {
                    for (int teamsCount = teamsPerRoom; teamsCount <= playersCount; teamsCount++) {
                        if (!isValidCombination(playersPerRoom, playersCount, teamsCount)) {
                            continue;
                        }

                        // given
                        Iterator<Integer> teamIterator = teamCycleIterator(teamsCount);
                        SettingsReader settings = settings(playersPerRoom, teamsPerRoom);
                        List<Deal> deals = createDeals(playersCount, teamIterator, settings);

                        // when
                        for (Deal deal : deals) {
                            spreader.fieldFor(deal, room, multiplayerType, playersPerRoom, 0, getField);
                        }

                        // then
                        boolean noFulfilledRoom = spreader.rooms().values().stream()
                                .allMatch(GameRoom::isFree);
                        if (noFulfilledRoom) {
                            failed = true;
                            System.err.println("\n" +
                                    "playersPerRoom:" + playersPerRoom + ", teamsPerRoom:" + teamsPerRoom + "\n" +
                                    "playersCount:" + playersCount + ", teamsCount:" + teamsCount + "\n" +
                                    roomReport());
                        }
                        spreader.rooms().clear();
                    }
                }
            }
        }
        assertEquals(false, failed);
    }

    private boolean isValidCombination(int playerPerRoom, int playersCount, int teamsCount) {
        int maxMembers = playersCount / teamsCount;
        if (playersCount % teamsCount != 0) {
            maxMembers++;
        }
        return playerPerRoom <= maxMembers;
    }

    private SettingsReader settings(int playerPerRoom, int teamsPerRoom) {
        return new RoundSettingsImpl()
                .integer(ROUNDS_PLAYERS_PER_ROOM, playerPerRoom)
                .integer(ROUNDS_TEAMS_PER_ROOM, teamsPerRoom);
    }

    private Iterator<Integer> teamCycleIterator(int teamsCount) {
        List<Integer> teams = new ArrayList<>(teamsCount);
        for (int index = 0; index < teamsCount; index++) {
            teams.add(index);
        }
        return Iterators.cycle(teams);
    }

    private List<Deal> createDeals(int playersCount, Iterator<Integer> teamIterator, SettingsReader settings) {
        List<Deal> result = new ArrayList<>();
        for (int index = 0; index < playersCount; index++) {
            result.add(newDeal(teamIterator.next(), settings));
        }
        return result;
    }

    private String roomReport() {
        return spreader.rooms().values().stream()
                .map(room -> room.deals().stream()
                        .map(player -> String.valueOf(player.getTeamId()))
                        .collect(Collectors.joining("-")))
                .collect(Collectors.joining(" | "));
    }

    @Test
    public void testGetGameRoom() {
        // given
        int roomSize = 10;
        SettingsReader settings = settings(roomSize, 1);

        Deal deal1 = newDeal(0, settings);
        Deal deal2 = newDeal(0, settings);

        spreader.fieldFor(deal1, room, multiplayerType, roomSize, 0, getField);
        spreader.fieldFor(deal2, room, multiplayerType, roomSize, 0, getField);

        // when
        Optional<GameRoom> optional1 = spreader.gameRoom(room, deal1.getPlayerId());
        Optional<GameRoom> optional2 = spreader.gameRoom(room, deal2.getPlayerId());

        // then
        assertEquals(true, optional1.isPresent());
        GameRoom room1 = optional1.get();

        assertEquals(true, optional2.isPresent());
        GameRoom room2 = optional2.get();

        assertSame(room1, room2);

        assertEquals(room, room1.room());
        assertEquals(2, room1.countPlayers());
        assertEquals(true, room1.containsPlayer(deal1.getPlayerId()));
        assertEquals(true, room1.containsPlayer(deal2.getPlayerId()));
        assertEquals(true, room1.containsDeal(deal1));
        assertEquals(true, room1.containsDeal(deal2));
        assertEquals(true, room1.containsTeam(0));
        assertEquals(false, room1.containsTeam(1));
        assertEquals(false, room1.isEmpty());
        assertEquals(true, room1.isFree());
        assertEquals(2, room1.countMembers(0));
        assertEquals(0, room1.countMembers(1));
        assertEquals(2, room1.countPlayers());
        assertEquals(true, room1.isFor(room1.field()));
        assertEquals(true, room1.deals().containsAll(Arrays.asList(deal1, deal2)));

        // when then
        assertEquals(false, spreader.gameRoom("otherRoom", deal1.getPlayerId()).isPresent());
        assertEquals(false, spreader.gameRoom(room, "otherPlayer").isPresent());
    }

    @Test
    public void testInformFieldService_whenAddAndRemove() {
        // given
        int roomSize = 2;
        SettingsReader settings = settings(roomSize, 1);
        assertEquals(0, fields.size());

        // when
        // register first player (1 of 2)
        Deal deal1 = newDeal(0, settings);
        spreader.fieldFor(deal1, room, multiplayerType, roomSize, 0, getField);

        // then
        assertEquals(1, fields.size());
        verify(fieldService).register(fields.get(0));
        reset();

        // when
        // register second player (2 of 2)
        Deal deal2 = newDeal(0, settings);
        spreader.fieldFor(deal2, room, multiplayerType, roomSize, 0, getField);

        // then
        assertEquals(1, fields.size());
        reset();

        // when
        // register third player in other field (1 of 2)
        Deal deal3 = newDeal(0, settings);
        spreader.fieldFor(deal3, room, multiplayerType, roomSize, 0, getField);

        // then
        assertEquals(2, fields.size());
        verify(fieldService).register(fields.get(1));
        reset();

        // when
        // remove player1 only (field is not empty)
        spreader.remove(deal1, Sweeper.off());

        // then
        assertEquals(2, fields.size());
        reset();

        // when
        // remove player2 only (field is empty)
        spreader.remove(deal2, Sweeper.off());

        // then
        assertEquals(2, fields.size());
        verify(fieldService).remove(fields.get(0));
        reset();

        // when
        // register forth player in other field (2 of 2)
        Deal deal4 = newDeal(0, settings);
        spreader.fieldFor(deal4, room, multiplayerType, roomSize, 0, getField);

        // then
        assertEquals(2, fields.size());
        reset();

        // when
        // remove player3 with player4 (field is empty)
        spreader.remove(deal3, Sweeper.on().allRemaining());

        // then
        assertEquals(2, fields.size());
        verify(fieldService).remove(fields.get(1));
        reset();
    }

    private void reset() {
        verifyNoMoreInteractions(fieldService);
        Mockito.reset(fieldService);
    }
}
