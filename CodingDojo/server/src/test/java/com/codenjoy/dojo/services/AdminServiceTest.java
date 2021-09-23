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
import com.codenjoy.dojo.config.Constants;
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.helper.Helpers;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.mocks.FirstInactivityGameType;
import com.codenjoy.dojo.services.mocks.SecondSemifinalGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.stuff.SmartAssert;
import io.cucumber.java.Before;
import org.junit.After;
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
import static com.codenjoy.dojo.services.helper.ChatDealsUtils.setupReadableName;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static com.codenjoy.dojo.utils.TestUtils.split;
import static java.util.stream.Collectors.joining;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class)
@ActiveProfiles(Constants.DATABASE_TYPE)
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
    private ChatService chat;

    @SpyBean
    private Registration registration;

    @Autowired
    private AdminService service;

    @Autowired
    private Helpers with;

    @Before
    public void setup() {
        with.clean.removeAll();
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
        setupReadableName(registration);

        players.register("player1", "first", "room1", "ip1");
        players.register("player2", "first", "room1", "ip2");
        players.register("player3", "first", "room2", "ip3");   // another room
        players.register("player4", "second", "room3", "ip4");  // another game

        assertPlayersLastResponse(players.getAll(),
                "[player1: 1000], [player2: 3000], [player3: 5000], [player4: 7000]");


        // field chat activity
        assertEquals("[1] player1_name at 2000: Player joined the field,\n" +
                        "[2] player2_name at 4000: Player joined the field,\n" +
                        "[3] player3_name at 6000: Player joined the field",
                allFieldChat());

        // when
        service.updateInactivity("room1",
                new InactivitySettingsImpl()
                        .setInactivityTimeout(123)
                        .setKickEnabled(true));

        // then
        assertPlayersLastResponse(players.getAll(),
                "[player1: 9000], [player2: 10000], [player3: 5000], [player4: 7000]");
    }

    private String allFieldChat() {
        return players.getAll().stream()
                .flatMap(player -> chat.getFieldMessages(player.getId(),
                        Filter.room(player.getRoom()).count(10).get()).stream())
                .map(message -> String.format("[%s] %s at %s: %s",
                        message.getId(),
                        message.getPlayerName(),
                        message.getTime(),
                        message.getText()))
                .collect(joining(",\n"));
    }

    public static void assertPlayersLastResponse(List<Player> players, String expected) {
        assertEquals(expected,
                players.stream()
                        .map(player -> String.format("[%s: %s]", player.getId(), player.getLastResponse()))
                        .collect(joining(", ")));
    }
}