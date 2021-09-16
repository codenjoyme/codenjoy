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
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.mocks.FirstInactivityGameType;
import com.codenjoy.dojo.services.mocks.SecondSemifinalGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.stuff.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.codenjoy.dojo.services.PlayerServiceImplTest.setupTimeService;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static com.codenjoy.dojo.utils.TestUtils.split;
import static java.util.stream.Collectors.joining;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class)
@ActiveProfiles(SQLiteProfile.NAME)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
public class AdminServiceTest {

    @SpyBean
    private TimeService time;

    @Autowired
    private PlayerService players;

    @Autowired
    private RoomService rooms;

    @Autowired
    private FieldService fields;

    @Autowired
    private AdminService service;

    @Before
    public void setup() {
        players.removeAll();
        rooms.removeAll();
        fields.removeAll();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldUpdateSettings_whenUpdateInactivity() {
        // given
        rooms.create("room", new FirstInactivityGameType());

        assertSettings("First[Parameter 1=15, \n" +
                "Parameter 2=true, \n" +
                "[Inactivity] Kick inactive players=false, \n" +
                "[Inactivity] Inactivity timeout ticks=300]");

        // when
        service.updateInactivity("room",
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
                split(rooms.settings("room"), ", \n"));
    }

    @Test
    public void shouldResetLastResponse_whenUpdateInactivity() {
        // given
        rooms.create("room1", new FirstInactivityGameType());
        rooms.create("room2", new FirstInactivityGameType());
        rooms.create("room3", new SecondSemifinalGameType());

        setupTimeService(time);

        players.register("player1", "first", "room1", "ip1");
        players.register("player2", "first", "room1", "ip2");
        players.register("player3", "first", "room2", "ip3");   // another room
        players.register("player4", "second", "room3", "ip4");  // another game

        assertPlayersLastResponse(this.players.getAll(),
                "[player1: 1000], [player2: 2000], [player3: 3000], [player4: 4000]");

        // when
        service.updateInactivity("room1",
                new InactivitySettingsImpl()
                        .setInactivityTimeout(123)
                        .setKickEnabled(true));

        // then
        assertPlayersLastResponse(this.players.getAll(),
                "[player1: 5000], [player2: 6000], [player3: 3000], [player4: 4000]");
    }

    public static void assertPlayersLastResponse(List<Player> players, String expected) {
        assertEquals(expected,
                players.stream()
                        .map(player -> String.format("[%s: %s]", player.getId(), player.getLastResponse()))
                        .collect(joining(", ")));
    }
}