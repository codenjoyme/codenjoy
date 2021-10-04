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
import com.codenjoy.dojo.client.local.DiceGenerator;
import com.codenjoy.dojo.config.Constants;
import com.codenjoy.dojo.config.RealGameConfiguration;
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.helper.Helpers;
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
@ActiveProfiles(Constants.DATABASE_TYPE)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
@Import(RealGameConfiguration.class)
public class ChatDealsTest {

    private final String game = "sample";
    private final String room = "room";
    private final String player1 = "player1";
    private final String player2 = "player2";

    // do not remove this - it used for spy-decorating for the component
    @SpyBean
    private GameService games;

    @Autowired
    private Helpers with;

    @Autowired
    private PlayerService players;

    @Autowired
    private TimerService timer;

    @Before
    public void setup() {
        with.login.asNone();
        with.clean.removeAll();

        // don't tick any services (we will tick PSI in manual mode)
        timer.pause();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    // only the player who caused this event will receive messages about events in the field chat.
    @Test
    public void shouldPostAllScoresMessagesIntoChat() {
        // given
        // mock dice for game in room
        with.rooms.mockDice(game, new DiceGenerator().getDice());

        // game type will be a multiple
        with.rooms.levelsSettings(room, game)
                .setLevelMaps(1,
                        "☼☼☼☼☼☼\n" +
                        "☼    ☼\n" +
                        "☼ $$ ☼\n" +
                        "☼ $$ ☼\n" +
                        "☼    ☼\n" +
                        "☼☼☼☼☼☼\n")
                .integer(WIN_SCORE, 30)
                .integer(LOSE_PENALTY, 100);


        with.time.nowIs(12345L);
        Deal deal1 = with.login.register(player1, "ip", room, game);
        with.login.asUser(player1, player1);
        int field1 = with.login.fieldId(player1);
        LazyJoystick joystick1 = deal1.getJoystick();

        with.time.nowIs(12346L);
        Deal deal2 = with.login.register(player2, "ip", room, game);
        with.login.asUser(player2, player2);
        int field2 = with.login.fieldId(player2);
        LazyJoystick joystick2 = deal2.getJoystick();

        assertEquals(field1, field2);

        // then
        with.chat.checkField(player1, room,
                "[PMessage(id=1, text=Player joined the field, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=Player joined the field, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        with.chat.checkField(player2, room,
                "[PMessage(id=1, text=Player joined the field, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=Player joined the field, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        with.chat.cleanField(player1, room);
        with.chat.cleanField(player2, room);

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

        with.chat.checkField(player1, room,
                "[PMessage(id=3, text=WIN => +30, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12346)]");

        with.chat.checkField(player2, room,
                "[]");

        with.chat.cleanField(player1, room);
        with.chat.cleanField(player2, room);

        // when
        joystick1.down();
        joystick2.down();
        players.tick();

        // then
        assertEquals("☼☼☼☼☼☼\n" +
                    "☼    ☼\n" +
                    "☼$$$ ☼\n" +
                    "☼ $  ☼\n" +
                    "☼Y ☺ ☼\n" +
                    "☼☼☼☼☼☼\n",
                deal1.getGame().getBoardAsString());

        with.chat.checkField(player1, room,
                "[]");

        with.chat.checkField(player2, room,
                "[PMessage(id=4, text=LOSE => 0, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        with.chat.cleanField(player1, room);
        with.chat.cleanField(player2, room);
    }
}
