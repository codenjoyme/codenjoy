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
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import org.junit.Test;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static org.junit.Assert.assertEquals;

public class GameRoomTest {

    private GamePlayer newGamePlayer() {
        return newGamePlayer(DEFAULT_TEAM_ID);
    }

    private GamePlayer newGamePlayer(int teamId) {
        GamePlayer player = new GamePlayer(event -> {}, new RoundSettingsImpl()) {};
        player.setTeamId(teamId);
        return player;
    }

    @Test
    public void isAvailable_noCapacity() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 2, true);
        room.join(newGamePlayer());
        room.join(newGamePlayer());

        assertEquals(false, room.isAvailable(newGamePlayer()));
    }

    @Test
    public void isAvailable_gameSettingsNotProvided() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 2, true);
        assertEquals(true, room.isAvailable(new GamePlayer(event -> {}, null) {}));
    }

    @Test
    public void isAvailable_gameSettingsNotHaveMaxTeamPerRoomParameter() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 2, true);
        assertEquals(true, room.isAvailable(new GamePlayer(event -> {}, new GameSettings()) {}));
    }

    @Test
    public void isAvailable_reachMaxTeamsCount() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 3, true);
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(1));
        assertEquals(false, room.isAvailable(newGamePlayer(2)));
    }

    @Test
    public void isAvailable_reachMaxTeamMembers() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 6, true);
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(1));
        room.join(newGamePlayer(1));
        assertEquals(false, room.isAvailable(newGamePlayer(0)));
        assertEquals(true, room.isAvailable(newGamePlayer(1)));
    }

    @Test
    public void isAvailable_oneDefaultTeam() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 4, true);
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(0));
        assertEquals(true, room.isAvailable(newGamePlayer(0)));
    }

    @Test
    public void isAvailable_unEvenCapacity() {
        GameRoom room = new GameRoom(NullGameField.INSTANCE, 7, true);
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(0));
        room.join(newGamePlayer(1));
        room.join(newGamePlayer(1));
        room.join(newGamePlayer(1));
        assertEquals(true, room.isAvailable(newGamePlayer(0)));
        assertEquals(true, room.isAvailable(newGamePlayer(1)));
    }
}
