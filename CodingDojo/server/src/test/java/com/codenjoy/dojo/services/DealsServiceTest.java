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
import com.codenjoy.dojo.services.helper.Helpers;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.stuff.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER5;
import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER6;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.toList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class)
@ActiveProfiles(Constants.DATABASE_TYPE)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
public class DealsServiceTest {

    @Autowired
    private Spreader spreader;

    @Autowired
    private FieldService fields;

    @Autowired
    protected Helpers with;

    @Before
    public void setup() {
        with.clean.removeAll();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldPlayersInField_playersInRoom_forSingleGame() {
        // given
        Deal deal1 = with.login.register("player1", "ip", "room", "first");
        with.login.join("player1", "room");

        Deal deal2 = with.login.register("player2", "ip", "room", "first");
        with.login.join("player2", "room");

        Deal deal3 = with.login.register("player3", "ip", "room", "first");
        with.login.join("player3", "room");

        int field1 = fields.id(deal1.getField());
        int field2 = fields.id(deal2.getField());
        int field3 = fields.id(deal3.getField());

        assertEquals("[1, 2, 3]", Arrays.asList(field1, field2, field3).toString());

        // when then
        assertSameFieldPlayers(field1, "[player1]");
        assertSameFieldPlayers(field2, "[player2]");
        assertSameFieldPlayers(field3, "[player3]");

        // when then
        assertSameRoomPlayers("room", "[player1, player2, player3]");
    }

    @Test
    public void shouldPlayersInField_playersInRoom_forSingleLevelsGame() {
        // given
        // will be SINGLE_LEVELS with 3 levels
        with.rooms.settings("room", "third")
                .bool(ROUNDS_ENABLED, false)
                .bool(PARAMETER6, true) // SINGLE_LEVELS
                .integer(PARAMETER5, 3); // 3 levels

        Deal deal1 = with.login.register("player1", "ip", "room", "third");
        with.login.join("player1", "room");

        Deal deal2 = with.login.register("player2", "ip", "room", "third");
        with.login.join("player2", "room");

        Deal deal3 = with.login.register("player3", "ip", "room", "third");
        with.login.join("player3", "room");

        int field1 = fields.id(deal1.getField());
        int field2 = fields.id(deal2.getField());
        int field3 = fields.id(deal3.getField());

        assertEquals("[1, 2, 3]", Arrays.asList(field1, field2, field3).toString());

        // when then
        assertSameFieldPlayers(field1, "[player1]");
        assertSameFieldPlayers(field2, "[player2]");
        assertSameFieldPlayers(field3, "[player3]");

        // when then
        assertSameRoomPlayers("room", "[player1, player2, player3]");
    }

    @Test
    public void shouldPlayersInField_playersInRoom_forMultiplayerGame() {
        // given
        with.rooms.settings("room", "third")
                .bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TEAMS_PER_ROOM, 1)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 2);

        Deal deal1 = with.login.register("player1", "ip", "room", "third");
        with.login.join("player1", "room");

        Deal deal2 = with.login.register("player2", "ip", "room", "third");
        with.login.join("player2", "room");

        Deal deal3 = with.login.register("player3", "ip", "room", "third");
        with.login.join("player3", "room");

        int field1 = fields.id(deal1.getField());
        int field2 = fields.id(deal2.getField());
        int field3 = fields.id(deal3.getField());

        assertEquals("[1, 1, 2]", Arrays.asList(field1, field2, field3).toString());

        // when then
        assertSameFieldPlayers(field1, "[player1, player2]");
        assertSameFieldPlayers(field2, "[player1, player2]");
        assertSameFieldPlayers(field3, "[player3]");

        // when then
        assertSameRoomPlayers("room", "[player1, player2, player3]");
    }

    private void assertSameFieldPlayers(int fieldId, String expected) {
        assertEquals(expected,
                spreader.players(fieldId).stream()
                        .map(Player::getId)
                        .collect(toList())
                        .toString());
    }

    private void assertSameRoomPlayers(String room, String expected) {
        assertEquals(expected,
                spreader.players(room).stream()
                        .map(Player::getId)
                        .collect(toList())
                        .toString());
    }
}