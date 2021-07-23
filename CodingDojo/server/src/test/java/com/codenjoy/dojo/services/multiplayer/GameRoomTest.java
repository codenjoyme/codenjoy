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

import com.codenjoy.dojo.services.mocks.GameSettings;
import com.codenjoy.dojo.services.nullobj.NullGameField;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import org.junit.Test;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_TEAMS_PER_ROOM;
import static org.junit.Assert.assertEquals;

public class GameRoomTest {

    private int playersPerRoom = 2;
    private int teamsPerRoom = 1;

    private GamePlayer newPlayer() {
        return newPlayer(DEFAULT_TEAM_ID);
    }

    private GamePlayer newPlayer(int teamId) {
        GamePlayer player = new GamePlayer(event -> {}, settings()) {};
        player.setTeamId(teamId);
        return player;
    }

    private RoundSettings settings() {
        RoundSettingsImpl settings = new RoundSettingsImpl();
        settings.getParameter(ROUNDS_PLAYERS_PER_ROOM.key()).update(playersPerRoom);
        settings.getParameter(ROUNDS_TEAMS_PER_ROOM.key()).update(teamsPerRoom);
        return settings;
    }

    private GameRoom createRoom() {
        return new GameRoom(NullGameField.INSTANCE, playersPerRoom, true);
    }

    @Test
    public void isAvailable_noCapacity() {
        // given
        GameRoom room = createRoom();

        // when
        room.join(newPlayer());
        room.join(newPlayer());

        // then
        assertEquals(false, room.isAvailable(newPlayer()));
    }

    @Test
    public void isAvailable_gameSettingsNotProvided() {
        // given
        GameRoom room = createRoom();
        GamePlayer player = new GamePlayer(event -> {}, null) {};

        // then
        assertEquals(true, room.isAvailable(player));
    }

    @Test
    public void isAvailable_gameSettingsNotHaveMaxTeamPerRoomParameter() {
        // given
        GameRoom room = createRoom();
        GamePlayer player = new GamePlayer(event -> {}, new GameSettings()) {};

        // then
        assertEquals(true, room.isAvailable(player));
    }

    @Test
    public void isAvailable_oneTeamPerRoom() {
        // given
        GameRoom room = createRoom();

        // then
        assertEquals(true, room.isAvailable(newPlayer()));
    }

    @Test
    public void isAvailable_reachMaxTeamsCount() {
        // given
        playersPerRoom = 3;
        teamsPerRoom = 2;
        GameRoom room = createRoom();

        // when
        room.join(newPlayer(0));
        room.join(newPlayer(1));

        // then
        assertEquals(false, room.isAvailable(newPlayer(2)));
    }

    @Test
    public void isAvailable_reachMaxTeamMembersCount_evenCapacity() {
        // given
        playersPerRoom = 6;
        teamsPerRoom = 2;
        GameRoom room = createRoom();

        // when
        room.join(newPlayer(0));
        room.join(newPlayer(0));
        room.join(newPlayer(0));
        room.join(newPlayer(1));
        room.join(newPlayer(1));

        // then
        assertEquals(false, room.isAvailable(newPlayer(0)));
        assertEquals(true, room.isAvailable(newPlayer(1)));
    }

    @Test
    public void isAvailable_reachMaxTeamMembersCount_unEvenCapacity() {
        // given
        playersPerRoom = 7;
        teamsPerRoom = 2;
        GameRoom room = createRoom();

        // when
        room.join(newPlayer(0));
        room.join(newPlayer(0));
        room.join(newPlayer(0));
        room.join(newPlayer(1));
        room.join(newPlayer(1));
        room.join(newPlayer(1));

        // then
        assertEquals(true, room.isAvailable(newPlayer(0)));
        assertEquals(true, room.isAvailable(newPlayer(1)));
    }
}
