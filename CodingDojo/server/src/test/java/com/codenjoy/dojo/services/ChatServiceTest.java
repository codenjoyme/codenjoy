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
import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.helper.ChatHelper;
import com.codenjoy.dojo.services.helper.LoginHelper;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.chat.ChatType.*;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.joining;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class)
@ActiveProfiles(SQLiteProfile.NAME)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
public class ChatServiceTest {

    @Autowired
    private ChatService service;

    @Autowired
    private Chat chat;

    @Autowired
    private Registration registration;

    @SpyBean
    private TimeService time;

    @Autowired
    private PlayerService players;

    @Autowired
    private ConfigProperties config;

    @Autowired
    private Deals deals;

    @SpyBean
    private FieldService fields;

    private LoginHelper login;
    private ChatHelper messages;

    private List<String> logs = new LinkedList<>();
    private List<ChatControl.OnChange> listeners = new LinkedList<>();
    private List<ChatControl> controls = new LinkedList<>();

    @Before
    public void setup() {
        login = new LoginHelper(config, players, registration, deals);
        messages = new ChatHelper(chat);

        messages.removeAll();
        fields.removeAll();
        login.removeAll();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldGetLastMessage() {
        // given
        // random values, don't look for systems here
        messages.post("room1", "player1", null, ROOM); // 1
        messages.post("room2", "player2", null, ROOM); // 2
        messages.post("room1", "player3", null, ROOM); // 3  last room1
        messages.post("room2", "player2", null, ROOM); // 4  last room2
        messages.post("room1", "player1", 1, TOPIC);   // 5
        messages.post("room2", "player2", 2, TOPIC);   // 6
        messages.post("room1", "player1", 1, TOPIC);   // 7  last topic1
        messages.post("room2", "player2", 2, TOPIC);   // 8
        messages.post("room1", "player2", 4, TOPIC);   // 9
        messages.post("room2", "player3", 3, TOPIC);   // 10 last topic3
        messages.post("room2", "player2", 2, TOPIC);   // 11 last topic2
        messages.post("room1", "player2", 4, TOPIC);   // 12 last topic4
        messages.post("room1", "player1", 1, FIELD);   // 13
        messages.post("room1", "player1", 1, FIELD);   // 14
        messages.post("room2", "player3", 3, FIELD);   // 15
        messages.post("room1", "player1", 1, FIELD);   // 16 last field1
        messages.post("room1", "player2", 4, FIELD);   // 17
        messages.post("room2", "player2", 2, FIELD);   // 18 last field2
        messages.post("room2", "player3", 3, FIELD);   // 19 last field3
        messages.post("room1", "player2", 4, FIELD);   // 20 last field4

        // when then
        ChatService.LastMessage last = service.getLast();
        assertEquals("ChatService.LastMessage(room={room1=3, room2=4}, " +
                        "topic={1=7, 3=10, 2=11, 4=12}, " +
                        "field={1=16, 2=18, 3=19, 4=20})",
                last.toString());

        // when then
        assertEquals("ChatService.Status(fieldId=1, lastInRoom=3, lastInField=16)",
                last.at(deal("room1", 1)).toString());

        assertEquals("ChatService.Status(fieldId=2, lastInRoom=3, lastInField=18)",
                last.at(deal("room1", 2)).toString());

        assertEquals("ChatService.Status(fieldId=3, lastInRoom=4, lastInField=19)",
                last.at(deal("room2", 3)).toString());

        assertEquals("ChatService.Status(fieldId=4, lastInRoom=4, lastInField=20)",
                last.at(deal("room2", 4)).toString());
    }

    private Deal deal(String room, int fieldId) {
        GameField field = mock(GameField.class);

        Game game = mock(Game.class);
        when(game.getField()).thenReturn(field);

        fields.register(field);
        when(fields.id(field)).thenReturn(fieldId);

        return new Deal(new Player(), game, room);
    }

    public void nowIs(long time) {
        when(this.time.now()).thenReturn(time);
    }

    @Test
    public void shouldGetControl() {
        // given
        givenThreePlayers();

        // when then
        // player1 create room message1
        nowIs(12345L);
        assertMessages(
                control(1).postRoom("message1", "room"),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345)]");

        assertListener(
                "listener1-player1 created: PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345),\n" +
                "listener1-player2 created: PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345)");

        // when then
        // player2 create room message2
        nowIs(12346L);
        assertMessages(
                control(2).postRoom("message2", "room"),
                "[PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertListener(
                "listener2-player1 created: PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346),\n" +
                "listener2-player2 created: PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)");

        // when then
        // player1 create topic message3 in the message1
        nowIs(12347L);
        assertMessages(
                control(1).postTopic(1, "message3", "room"),
                "[PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347)]");

        assertListener(
                "listener1-player1 created: PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347),\n" +
                "listener1-player2 created: PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347)");

        // when then
        // player2 create topic message4 in the message2
        nowIs(12348L);
        assertMessages(
                control(2).postTopic(2, "message4", "room"),
                "[PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)]");

        assertListener(
                "listener2-player1 created: PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348),\n" +
                "listener2-player2 created: PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)");

        // when then
        // player1 create field message5
        nowIs(12349L);
        assertMessages(
                control(1).postField("message5", "room"),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener1-player1 created: PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)");

        // when then
        // player2 create field message6
        nowIs(12350L);
        assertMessages(
                control(2).postField("message6", "room"),
                "[PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)]");

        assertListener(
                "listener2-player2 created: PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)");

        // when then
        // player1 create topic field message7 in the field message5
        nowIs(12351L);
        assertMessages(
                control(1).postTopic(5, "message7", "room"),
                "[PMessage(id=7, text=message7, room=room, type=2, topicId=5, \n" +
                "    playerId=player1, playerName=player1-name, time=12351)]");

        assertListener(
                "listener1-player1 created: PMessage(id=7, text=message7, room=room, type=2, topicId=5, \n" +
                "    playerId=player1, playerName=player1-name, time=12351),\n" +
                "listener1-player2 created: PMessage(id=7, text=message7, room=room, type=2, topicId=5, \n" +
                "    playerId=player1, playerName=player1-name, time=12351)");

        // when then
        // player2 create topic field message8 in the field message6
        nowIs(12352L);
        assertMessages(
                control(2).postTopic(6, "message8", "room"),
                "[PMessage(id=8, text=message8, room=room, type=2, topicId=6, \n" +
                "    playerId=player2, playerName=player2-name, time=12352)]");

        assertListener(
                "listener2-player1 created: PMessage(id=8, text=message8, room=room, type=2, topicId=6, \n" +
                "    playerId=player2, playerName=player2-name, time=12352),\n" +
                "listener2-player2 created: PMessage(id=8, text=message8, room=room, type=2, topicId=6, \n" +
                "    playerId=player2, playerName=player2-name, time=12352)");

        // when then
        // player1 get room message1
        assertMessages(
                control(1).get(1, "room"),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                        "    playerId=player1, playerName=player1-name, time=12345)]");

        assertListener("");

        // when then
        // player2 get topic message5
        assertMessages(
                control(2).get(5, "room"),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener("");

        // given
        Filter filter = Filter
                .room("room")
                .afterId(1)
                .beforeId(10)
                .inclusive(true)
                .count(10)
                .get();

        // when then
        // player1 get all room messages
        assertMessages(control(1).getAllRoom(filter),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertListener("");

        // when then
        // player2 get all room messages
        assertMessages(control(2).getAllRoom(filter),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertListener("");

        // when then
        // player1 get all topic messages for room message1
        assertMessages(
                control(1).getAllTopic(1, filter),
                "[PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347)]");

        assertListener("");

        // when then
        // player2 get all room messages for room message2
        assertMessages(
                control(2).getAllTopic(2, filter),
                "[PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)]");

        assertListener("");

        // when then
        // player1 get all field messages
        assertMessages(
                control(1).getAllField(filter),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener("");

        // when then
        // player2 get all field messages
        assertMessages(
                control(2).getAllField(filter),
                "[PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)]");

        assertListener("");

        // when then
        // player2 delete field message6
        assertEquals(true,
                control(2).delete(6, "room"));

        assertListener(
                "listener2-player2 deleted: PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)");

        // when then
        // player1 get all field messages
        assertMessages(
                control(1).getAllField(filter),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener("");

        // when then
        // player2 get all field messages
        assertMessages(control(2).getAllField(filter), "[]");

        assertListener("");

        // when then
        // player1 delete topic message3
        assertEquals(true,
                control(1).delete(3, "room"));

        assertListener(
                "listener1-player1 deleted: PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347),\n" +
                "listener1-player2 deleted: PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347)");

        // when then
        // player1 get all topic messages for room message1
        assertMessages(control(1).getAllTopic(1, filter), "[]");

        assertListener("");

        // when then
        // player2 get all topic messages for room message2
        assertMessages(
                control(2).getAllTopic(2, filter),
                "[PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)]");

        assertListener("");

        // when then
        // player2 delete topic field message8
        assertEquals(true,
                control(2).delete(8, "room"));

        assertListener(
                "listener2-player1 deleted: PMessage(id=8, text=message8, room=room, type=2, topicId=6, \n" +
                "    playerId=player2, playerName=player2-name, time=12352),\n" +
                "listener2-player2 deleted: PMessage(id=8, text=message8, room=room, type=2, topicId=6, \n" +
                "    playerId=player2, playerName=player2-name, time=12352)");
    }

    private void assertMessages(PMessage message, String expected) {
        assertMessages(Arrays.asList(message), expected);
    }

    private void assertMessages(List<PMessage> messages, String expected) {
        assertEquals(expected,
                messages.toString()
                        .replace(", playerId", ", \n    playerId")
                        .replace("), PMessage(", "), \nPMessage("));
    }

    private ChatControl control(int id) {
        return controls.get(id - 1);
    }

    private void givenThreePlayers() {
        createPlayerWithControl("player1", "room", "first");
        createPlayerWithControl("player2", "room", "first");
        createPlayerWithControl("player3", "room2", "first");
    }

    private void createPlayerWithControl(String player, String room, String game) {
        int index = listeners.size();
        login.register(player, "ip", room, game);
        login.join(player, room);
        listeners.add(getListener(index + 1));
        controls.add(service.control(player, listeners.get(index)));
    }

    private void assertListener(String expected) {
        assertEquals(expected, logs.stream()
                .collect(joining(",\n"))
                .replace(", playerId", ", \n    playerId")
                .replace(", listener", ", \nlistener"));
        logs.clear();
    }

    private ChatControl.OnChange getListener(int id) {
        return new ChatControl.OnChange() {
            @Override
            public void deleted(PMessage message, String playerId) {
                logs.add(String.format("listener%s-%s deleted: %s",
                        id, playerId, message));
            }

            @Override
            public void created(PMessage message, String playerId) {
                logs.add(String.format("listener%s-%s created: %s",
                        id, playerId, message));
            }
        };
    }
}