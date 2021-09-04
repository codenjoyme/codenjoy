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
import com.codenjoy.dojo.services.mocks.GameSettings;
import com.codenjoy.dojo.services.nullobj.NullGameField;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.settings.SettingsReader;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_TEAMS_PER_ROOM;
import static org.junit.Assert.assertEquals;

public class GameRoomTest {

    public static final String ROOM = "room";
    private int playersPerRoom = 2;
    private int teamsPerRoom = 1;
    private List<Player> players = new LinkedList<>();

    private Deal newDeal() {
        return newDeal(DEFAULT_TEAM_ID, settings());
    }

    private Deal newDeal(int teamId) {
        return newDeal(teamId, settings());
    }

    private Deal newDeal(int teamId, SettingsReader settings) {
        GamePlayer gamePlayer = new GamePlayer(event -> {}, settings) {};
        Game game = new Single(gamePlayer, null);
        Deal result = new Deal(newPlayer(), game, ROOM);
        result.setTeamId(teamId);
        return result;
    }

    private Player newPlayer() {
        Player result = new Player("player" + players.size());
        players.add(result);
        return result;
    }

    private SettingsReader settings() {
        return new RoundSettingsImpl()
                .integer(ROUNDS_PLAYERS_PER_ROOM, playersPerRoom)
                .integer(ROUNDS_TEAMS_PER_ROOM, teamsPerRoom);
    }

    private GameRoom createRoom() {
        return new GameRoom(ROOM, NullGameField.INSTANCE, playersPerRoom, true);
    }

    @Test
    public void testGetRoom() {
        // given
        GameRoom room = createRoom();

        // when then
        assertEquals(ROOM, room.room());
    }

    @Test
    public void isAvailable_noCapacity() {
        // given
        GameRoom room = createRoom();

        // when
        room.join(newDeal());
        room.join(newDeal());

        // then
        assertEquals(false, room.isAvailable(newDeal()));
    }

    @Test
    public void isAvailable_gameSettingsNotProvided() {
        // given
        GameRoom room = createRoom();

        Deal deal = newDeal(0, null);

        // then
        assertEquals(true, room.isAvailable(deal));
    }

    @Test
    public void isAvailable_gameSettingsNotHaveMaxTeamPerRoomParameter() {
        // given
        GameRoom room = createRoom();

        Deal deal = newDeal(0, new GameSettings());

        // then
        assertEquals(true, room.isAvailable(deal));
    }

    @Test
    public void isAvailable_oneTeamPerRoom() {
        // given
        GameRoom room = createRoom();

        // then
        assertEquals(true, room.isAvailable(newDeal()));
    }

    @Test
    public void isAvailable_reachMaxTeamsCount() {
        // given
        playersPerRoom = 3;
        teamsPerRoom = 2;
        GameRoom room = createRoom();

        // when
        room.join(newDeal(0));
        room.join(newDeal(1));

        // then
        assertEquals(false, room.isAvailable(newDeal(2)));
    }

    @Test
    public void isAvailable_reachMaxTeamMembersCount_evenCapacity() {
        // given
        playersPerRoom = 6;
        teamsPerRoom = 2;
        GameRoom room = createRoom();

        // when
        room.join(newDeal(0));
        room.join(newDeal(0));
        room.join(newDeal(0));
        room.join(newDeal(1));
        room.join(newDeal(1));

        // then
        assertEquals(false, room.isAvailable(newDeal(0)));
        assertEquals(true, room.isAvailable(newDeal(1)));
    }

    @Test
    public void isAvailable_reachMaxTeamMembersCount_unEvenCapacity() {
        // given
        playersPerRoom = 7;
        teamsPerRoom = 2;
        GameRoom room = createRoom();

        // when
        room.join(newDeal(0));
        room.join(newDeal(0));
        room.join(newDeal(0));
        room.join(newDeal(1));
        room.join(newDeal(1));
        room.join(newDeal(1));

        // then
        assertEquals(true, room.isAvailable(newDeal(0)));
        assertEquals(true, room.isAvailable(newDeal(1)));
    }
}
