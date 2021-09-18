package com.codenjoy.dojo.services.chat;

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
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.config.Constants;
import com.codenjoy.dojo.config.RealGameConfiguration;
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.LazyJoystick;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.helper.ChatHelper;
import com.codenjoy.dojo.services.helper.LoginHelper;
import com.codenjoy.dojo.services.helper.RoomHelper;
import com.codenjoy.dojo.services.helper.TimeHelper;
import com.codenjoy.dojo.stuff.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class,
        properties = Constants.ALLOW_OVERRIDING)
@ActiveProfiles(SQLiteProfile.NAME)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
@Import(RealGameConfiguration.class)
public class ChatDealsTest {

    private final String game = "sample";
    private final String room = "room";
    private final String player1 = "player1";
    private final String player2 = "player2";

    @Autowired
    private RoomHelper roomsSettings;

    @SpyBean
    private GameService games;

    @Autowired
    private LoginHelper login;

    @Autowired
    private TimeHelper time;

    @Autowired
    private ChatHelper messages;

    @Autowired
    private PlayerService players;

    @Autowired
    private ChatService chat;

    @Before
    public void setup() {
        login.asNone();
        login.removeAll();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldPostAllScoresMessagesIntoChat() {
        // given
        // mock dice for game in room
        roomsSettings.mockDice(game,
                LocalGameRunner.getDice("435874345435874365843564398", 100, 200));

        // game type will be a multiple
        roomsSettings.settings(room, game)
                .string(LEVEL_MAP,
                        "☼☼☼☼☼☼\n" +
                        "☼    ☼\n" +
                        "☼ $$ ☼\n" +
                        "☼ $$ ☼\n" +
                        "☼    ☼\n" +
                        "☼☼☼☼☼☼\n")
                .integer(WIN_SCORE, 30)
                .integer(LOSE_PENALTY, 100);


        time.nowIs(12345L);
        Deal deal1 = login.register(player1, "ip", room, game);
        login.asUser(player1, player1);
        int field1 = login.fieldId(player1);
        LazyJoystick joystick1 = deal1.getJoystick();

        time.nowIs(12346L);
        Deal deal2 = login.register(player2, "ip", room, game);
        login.asUser(player2, player2);
        int field2 = login.fieldId(player2);
        LazyJoystick joystick2 = deal2.getJoystick();

        assertEquals(field1, field2);

        // then
        messages.checkField(player1, room,
                "[PMessage(id=1, text=Player joined the field, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=Player joined the field, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertEquals("☼☼☼☼☼☼\n" +
                    "☼    ☼\n" +
                    "☼ $$ ☼\n" +
                    "☼ $$ ☼\n" +
                    "☼☻ ☺ ☼\n" +
                    "☼☼☼☼☼☼\n",
                deal1.getGame().getBoardAsString());

        // when
        joystick1.up();
        joystick2.act();
        joystick2.up();
        players.tick();

        // then
        assertEquals("☼☼☼☼☼☼\n" +
                    "☼    ☼\n" +
                    "☼$$$ ☼\n" +
                    "☼☻$☺ ☼\n" +
                    "☼x   ☼\n" +
                    "☼☼☼☼☼☼\n",
                deal1.getGame().getBoardAsString());

        messages.checkField(player1, room,
                "[PMessage(id=3, text=WIN => +30, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12346)]");

        // when
        joystick1.down();
        joystick2.down();
        players.tick();

        // then
        assertEquals("☼☼☼☼☼☼\n" +
                    "☼    ☼\n" +
                    "☼$$$ ☼\n" +
                    "☼ $  ☼\n" +
                    "☼X ☺ ☼\n" +
                    "☼☼☼☼☼☼\n",
                deal1.getGame().getBoardAsString());

        messages.checkField(player1, room,
                "[PMessage(id=4, text=LOSE => 0, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");
    }
}
