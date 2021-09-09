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

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.mocks.FirstInactivityGameType;
import com.codenjoy.dojo.services.mocks.SecondSemifinalGameType;
import com.codenjoy.dojo.services.room.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PlayerServiceImplTest.setupTimeService;
import static com.codenjoy.dojo.utils.TestUtils.split;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
public class AdminServiceTest {

    @SpyBean
    private TimeService timeService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AdminService adminService;

    @Test
    public void shouldUpdateSettings_whenUpdateInactivity() {
        // given
        roomService.create("room", new FirstInactivityGameType());

        assertSettings("First[Parameter 1=15, \n" +
                "Parameter 2=true, \n" +
                "[Inactivity] Kick inactive players=false, \n" +
                "[Inactivity] Inactivity timeout ticks=300]");

        // when
        adminService.updateInactivity("room",
                new InactivitySettingsImpl()
                        .setInactivityTimeout(123)
                        .setKickEnabled(true));

        // then
        assertSettings("First[Parameter 1=15, \n" +
                        "Parameter 2=true, \n" +
                        "[Inactivity] Kick inactive players=true, \n" +
                        "[Inactivity] Inactivity timeout ticks=123]");
    }

    private void assertSettings(String expected) {
        assertEquals(expected,
                split(roomService.settings("room"), ", \n"));
    }

    @Test
    public void shouldResetLastResponse_whenUpdateInactivity() {
        // given
        roomService.create("room1", new FirstInactivityGameType());
        roomService.create("room2", new FirstInactivityGameType());
        roomService.create("room3", new SecondSemifinalGameType());

        setupTimeService(timeService);

        List<Player> players = new LinkedList<>();
        players.add(playerService.register("player1", "first", "room1", "ip1"));
        players.add(playerService.register("player2", "first", "room1", "ip2"));
        players.add(playerService.register("player3", "first", "room2", "ip3"));   // another room
        players.add(playerService.register("player4", "second", "room3", "ip4"));  // another game

        assertPlayersLastResponse(playerService.getAll(),
                "[player1: 1000], [player2: 2000], [player3: 3000], [player4: 4000]");

        // when
        adminService.updateInactivity("room1",
                new InactivitySettingsImpl()
                        .setInactivityTimeout(123)
                        .setKickEnabled(true));

        // then
        assertPlayersLastResponse(playerService.getAll(),
                "[player1: 5000], [player2: 6000], [player3: 3000], [player4: 4000]");
    }

    public static void assertPlayersLastResponse(List<Player> players, String expected) {
        assertEquals(expected,
                players.stream()
                        .map(player -> String.format("[%s: %s]", player.getId(), player.getLastResponse()))
                        .collect(joining(", ")));
    }
}
