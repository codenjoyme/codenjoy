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
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.nullobj.NullGameField;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import com.google.common.collect.Iterators;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_TEAMS_PER_ROOM;
import static org.junit.Assert.assertEquals;

public class SpreaderTest {

    private final Spreader spreader = new Spreader();

    private final String room = "room";
    private final MultiplayerType multiplayerType = MultiplayerType.MULTIPLE;
    private final Supplier<GameField> gameFiled = () -> NullGameField.INSTANCE;
    private List<Player> players = new LinkedList<>();

    private Player newPlayer() {
        Player result = new Player("player" + players.size());
        players.add(result);
        return result;
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
                            spreader.fieldFor(deal, room, multiplayerType, playersPerRoom, 0, gameFiled);
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
}
