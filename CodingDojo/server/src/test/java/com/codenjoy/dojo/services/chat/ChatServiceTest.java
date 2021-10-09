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
import com.codenjoy.dojo.config.Constants;
import com.codenjoy.dojo.config.TestSqliteDBLocations;
import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.FieldService;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.helper.Helpers;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.utils.smart.SmartAssert;
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

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.services.chat.ChatType.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static java.util.stream.Collectors.joining;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class)
@ActiveProfiles(Constants.DATABASE_TYPE)
@ContextConfiguration(initializers = TestSqliteDBLocations.class)
public class ChatServiceTest {

    public static final int MAX = 100;
    @Autowired
    private ChatService service;

    @Autowired
    private Chat chat;

    @SpyBean
    private FieldService fields;

    @Autowired
    private Helpers with;

    private List<String> logs = new LinkedList<>();
    private List<OnChange> listeners = new LinkedList<>();
    private List<ChatAuthority> chats = new LinkedList<>();

    @Before
    public void setup() {
        with.clean.removeAll();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldGetLastMessage() {
        // given
        // random values, don't look for systems here
        with.chat.post("room1", "player1", null, ROOM); // 1
        with.chat.post("room2", "player2", null, ROOM); // 2
        with.chat.post("room1", "player3", null, ROOM); // 3  last room1
        with.chat.post("room2", "player2", null, ROOM); // 4  last room2

        with.chat.post("room1", "player1", 1, ROOM_TOPIC);   // 5
        with.chat.post("room2", "player2", 2, ROOM_TOPIC);   // 6
        with.chat.post("room1", "player1", 1, ROOM_TOPIC);   // 7  last room topic1
        with.chat.post("room2", "player2", 2, ROOM_TOPIC);   // 8
        with.chat.post("room1", "player2", 4, ROOM_TOPIC);   // 9
        with.chat.post("room2", "player3", 3, ROOM_TOPIC);   // 10 last room topic3
        with.chat.post("room2", "player2", 2, ROOM_TOPIC);   // 11 last room topic2
        with.chat.post("room1", "player2", 4, ROOM_TOPIC);   // 12 last room topic4

        with.chat.post("room1", "player1", 1, FIELD);   // 13
        with.chat.post("room1", "player1", 1, FIELD);   // 14
        with.chat.post("room2", "player3", 3, FIELD);   // 15
        with.chat.post("room1", "player1", 1, FIELD);   // 16 last field1
        with.chat.post("room1", "player2", 4, FIELD);   // 17
        with.chat.post("room2", "player2", 2, FIELD);   // 18 last field2
        with.chat.post("room2", "player3", 3, FIELD);   // 19 last field3
        with.chat.post("room1", "player2", 4, FIELD);   // 20 last field4

        with.chat.post("room1", "player1", 13, FIELD_TOPIC);   // 21
        with.chat.post("room1", "player1", 13, FIELD_TOPIC);   // 22
        with.chat.post("room2", "player3", 15, FIELD_TOPIC);   // 23
        with.chat.post("room1", "player1", 16, FIELD_TOPIC);   // 24
        with.chat.post("room1", "player2", 16, FIELD_TOPIC);   // 25 last field topic15
        with.chat.post("room2", "player2", 15, FIELD_TOPIC);   // 26
        with.chat.post("room2", "player3", 15, FIELD_TOPIC);   // 27 last field topic15
        with.chat.post("room1", "player2", 13, FIELD_TOPIC);   // 28 last field topic13

        // when then
        ChatService.LastMessage last = service.getLast();
        assertEquals("ChatService.LastMessage(" +
                        "room={room1=3, room2=4}, " +
                        "roomTopic={1=7, 3=10, 2=11, 4=12}, " +
                        "field={1=16, 2=18, 3=19, 4=20}, " +
                        "fieldTopic={16=25, 15=27, 13=28})",
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

    @Test
    public void shouldGetControl() {
        // given
        givenThreePlayers();

        // when then
        // player1 create room message1
        with.time.nowIs(12345L);
        with.chat.check(
                chat(0).postRoom("message1", "room"),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345)]");

        assertListener(
                "listener1-player1 created in room: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "        playerId=player1, playerName=player1-name, time=12345)],\n" +
                "listener1-player2 created in room: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "        playerId=player1, playerName=player1-name, time=12345)]");

        // when then
        // player2 create room message2
        with.time.nowIs(12346L);
        with.chat.check(
                chat(1).postRoom("message2", "room"),
                "[PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertListener(
                "listener2-player1 created in room: [\n" +
                "    PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "        playerId=player2, playerName=player2-name, time=12346)],\n" +
                "listener2-player2 created in room: [\n" +
                "    PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "        playerId=player2, playerName=player2-name, time=12346)]");

        // when then
        // player1 create topic message3 in the message1
        with.time.nowIs(12347L);
        with.chat.check(
                chat(0).postTopic(1, "message3", "room"),
                "[PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347)]");

        assertListener(
                "listener1-player1 created in room: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12347)],\n" +
                "listener1-player2 created in room: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12347)]");

        // when then
        // player2 create topic message4 in the message2
        with.time.nowIs(12348L);
        with.chat.check(
                chat(1).postTopic(2, "message4", "room"),
                "[PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)]");

        assertListener(
                "listener2-player1 created in room: [\n" +
                "    PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12348)],\n" +
                "listener2-player2 created in room: [\n" +
                "    PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12348)]");

        // when then
        // player1 create field message5
        with.time.nowIs(12349L);
        with.chat.check(
                chat(0).postField("message5", "room"),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)]");

        // when then
        // player2 create field message6
        with.time.nowIs(12350L);
        with.chat.check(
                chat(1).postField("message6", "room"),
                "[PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)]");

        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12350)]");

        // when then
        // player1 create topic field message7 in the field message5
        with.time.nowIs(12351L);
        with.chat.check(
                chat(0).postTopic(5, "message7", "room"),
                "[PMessage(id=7, text=message7, room=room, type=4, topicId=5, \n" +
                "    playerId=player1, playerName=player1-name, time=12351)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=7, text=message7, room=room, type=4, topicId=5, \n" +
                "        playerId=player1, playerName=player1-name, time=12351)],\n" +
                "listener1-player2 created in field: [\n" +
                "    PMessage(id=7, text=message7, room=room, type=4, topicId=5, \n" +
                "        playerId=player1, playerName=player1-name, time=12351)]");

        // when then
        // player2 create topic field message8 in the field message6
        with.time.nowIs(12352L);
        with.chat.check(
                chat(1).postTopic(6, "message8", "room"),
                "[PMessage(id=8, text=message8, room=room, type=4, topicId=6, \n" +
                "    playerId=player2, playerName=player2-name, time=12352)]");

        assertListener(
                "listener2-player1 created in field: [\n" +
                "    PMessage(id=8, text=message8, room=room, type=4, topicId=6, \n" +
                "        playerId=player2, playerName=player2-name, time=12352)],\n" +
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=8, text=message8, room=room, type=4, topicId=6, \n" +
                "        playerId=player2, playerName=player2-name, time=12352)]");

        // when then
        // player1 get room message1
        with.chat.check(
                chat(0).get(1, "room"),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                        "    playerId=player1, playerName=player1-name, time=12345)]");

        assertListener(
                "listener1-player1 created in room: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "        playerId=player1, playerName=player1-name, time=12345)]");

        // when then
        // player2 get topic message5
        with.chat.check(
                chat(1).get(5, "room"),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)]");

        // given
        Filter filter = Filter
                .room("room")
                .afterId(1)
                .beforeId(MAX)
                .inclusive(true)
                .count(MAX)
                .get();

        // when then
        // player1 get all room messages
        with.chat.check(chat(0).getAllRoom(filter),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertListener(
                "listener1-player1 created in room: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "        playerId=player1, playerName=player1-name, time=12345), \n" +
                "    PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "        playerId=player2, playerName=player2-name, time=12346)]");

        // when then
        // player2 get all room messages
        with.chat.check(chat(1).getAllRoom(filter),
                "[PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "    playerId=player1, playerName=player1-name, time=12345), \n" +
                "PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "    playerId=player2, playerName=player2-name, time=12346)]");

        assertListener(
                "listener2-player2 created in room: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=1, topicId=null, \n" +
                "        playerId=player1, playerName=player1-name, time=12345), \n" +
                "    PMessage(id=2, text=message2, room=room, type=1, topicId=null, \n" +
                "        playerId=player2, playerName=player2-name, time=12346)]");

        // when then
        // player1 get all topic messages for room message1
        with.chat.check(
                chat(0).getAllTopic(1, filter),
                "[PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12347)]");

        assertListener(
                "listener1-player1 created in room: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12347)]");

        // when then
        // player2 get all room messages for room message2
        with.chat.check(
                chat(1).getAllTopic(2, filter),
                "[PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)]");

        assertListener(
                "listener2-player2 created in room: [\n" +
                "    PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12348)]");

        // when then
        // player1 get all field messages
        with.chat.check(
                chat(0).getAllField(filter),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)]");

        // when then
        // player2 get all field messages
        with.chat.check(
                chat(1).getAllField(filter),
                "[PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)]");

        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12350)]");

        // when then
        // player2 delete field message6
        assertEquals(true,
                chat(1).delete(6, "room"));

        // only player2 will receive update, because of each player on their own field
        assertListener(
                "listener2-player2 deleted in field: [\n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12350)]");

        // when then
        // player1 get all field messages
        with.chat.check(
                chat(0).getAllField(filter),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)]");

        // when then
        // player2 get all field messages
        with.chat.check(chat(1).getAllField(filter),
                "[]");

        assertListener(
                "");

        // when then
        // player1 delete topic message3
        assertEquals(true,
                chat(0).delete(3, "room"));

        assertListener(
                "listener1-player1 deleted in room: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12347)],\n" +
                "listener1-player2 deleted in room: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=2, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12347)]");

        // when then
        // player1 get all topic messages for room message1
        with.chat.check(chat(0).getAllTopic(1, filter),
                "[]");

        assertListener(
                "");

        // when then
        // player2 get all topic messages for room message2
        with.chat.check(
                chat(1).getAllTopic(2, filter),
                "[PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12348)]");

        assertListener(
                "listener2-player2 created in room: [\n" +
                "    PMessage(id=4, text=message4, room=room, type=2, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12348)]");

        // when then
        // player2 delete topic field message8
        assertEquals(true,
                chat(1).delete(8, "room"));

        // only player2 will receive update, because of each player on their own field
        assertListener(
                "listener2-player2 deleted in field: [\n" +
                "    PMessage(id=8, text=message8, room=room, type=4, topicId=6, \n" +
                "        playerId=player2, playerName=player2-name, time=12352)]");

        // when then
        // get empty list in room
        with.chat.check(chat(0).getAllRoom(Filter.room("room").count(0).get()),
                "[]");

        assertListener(
                "");
    }

    @Test
    public void shouldGetPersonalFieldMessages_caseSingle() {
        // given
        String singleGame = "first";

        Deal deal1 = createPlayerWithControl("player1", "room", singleGame);
        Deal deal2 = createPlayerWithControl("player2", "room", singleGame);
        Deal deal3 = createPlayerWithControl("player3", "otherRoom", singleGame);

        assertEquals("[1, 2, 3]", Arrays.asList(
                fields.id(deal1.getField()),
                fields.id(deal2.getField()),
                fields.id(deal3.getField())).toString());

        // when then
        // player1 create field message1
        with.time.nowIs(12349L);
        with.chat.check(
                chat(0).postField("message1", "room"),
                "[PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)]");

        // when then
        // player2 create field message2
        with.time.nowIs(12350L);
        with.chat.check(
                chat(1).postField("message2", "room"),
                "[PMessage(id=2, text=message2, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)]");

        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=2, text=message2, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12350)]");

        // when then
        // player1 create personal field message3 for player2
        with.time.nowIs(12351L);
        with.chat.check(
                chat(0).postFieldFor("player2", "message3", "room"),
                "[PMessage(id=3, text=message3, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12351)]");

        // for other player
        assertListener(
                "listener1-player2 created in field: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12351)]");

        // when then
        // player2 create personal field message4 for player1
        with.time.nowIs(12352L);
        with.chat.check(
                chat(1).postFieldFor("player1", "message4", "room"),
                "[PMessage(id=4, text=message4, room=room, type=3, topicId=2, \n" +
                        "    playerId=player2, playerName=player2-name, time=12352)]");

        // for other player
        assertListener(
                "listener2-player1 created in field: [\n" +
                "    PMessage(id=4, text=message4, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12352)]");

        // when then
        // player1 create personal field message5 for itself
        with.time.nowIs(12353L);
        with.chat.check(
                chat(0).postFieldFor("player1", "message5", "room"),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12353)]");

        // for itself
        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12353)]");

        // when then
        // player2 create personal field message6 for itself
        with.time.nowIs(12354L);
        with.chat.check(
                chat(1).postFieldFor("player2", "message6", "room"),
                "[PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12354)]");

        // for itself
        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12354)]");

        // given
        Filter filter = Filter
                .room("room")
                .afterId(1)
                .beforeId(MAX)
                .inclusive(true)
                .count(MAX)
                .get();

        // when then
        // player1 get all field messages
        with.chat.check(
                chat(0).getAllField(filter),
                "[PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349), \n" +
                "PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12353)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349), \n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12353)]");

        // when then
        // player2 get all field messages
        with.chat.check(
                chat(1).getAllField(filter),
                "[PMessage(id=2, text=message2, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12350), \n" +
                "PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "    playerId=player2, playerName=player2-name, time=12354)]");

        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=2, text=message2, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12350), \n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=2, \n" +
                "        playerId=player2, playerName=player2-name, time=12354)]");
    }

    @Test
    public void shouldGetPersonalFieldMessages_caseMultiple() {
        // given
        String multipleGame = "third";

        // given
        with.rooms.settings("room", "third")
                .bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TEAMS_PER_ROOM, 1)
                .integer(ROUNDS_PLAYERS_PER_ROOM, 2);

        Deal deal1 = createPlayerWithControl("player1", "room", multipleGame);
        Deal deal2 = createPlayerWithControl("player2", "room", multipleGame);
        Deal deal3 = createPlayerWithControl("player3", "otherRoom", multipleGame);

        // TODO extract to helper
        assertEquals("[1, 1, 2]", Arrays.asList(
                fields.id(deal1.getField()),
                fields.id(deal2.getField()),
                fields.id(deal3.getField())).toString());

        // when then
        // player1 create field message1
        with.time.nowIs(12349L);
        with.chat.check(
                chat(0).postField("message1", "room"),
                "[PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)],\n" +
                "listener1-player2 created in field: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349)]");

        // when then
        // player2 create field message2
        with.time.nowIs(12350L);
        with.chat.check(
                chat(1).postField("message2", "room"),
                "[PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12350)]");

        assertListener(
                "listener2-player1 created in field: [\n" +
                "    PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12350)],\n" +
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12350)]");

        // when then
        // player1 create personal field message3 for player2
        with.time.nowIs(12351L);
        with.chat.check(
                chat(0).postFieldFor("player2", "message3", "room"),
                "[PMessage(id=3, text=message3, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12351)]");

        // for other player
        assertListener(
                "listener1-player2 created in field: [\n" +
                "    PMessage(id=3, text=message3, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12351)]");

        // when then
        // player2 create personal field message4 for player1
        with.time.nowIs(12352L);
        with.chat.check(
                chat(1).postFieldFor("player1", "message4", "room"),
                "[PMessage(id=4, text=message4, room=room, type=3, topicId=1, \n" +
                        "    playerId=player2, playerName=player2-name, time=12352)]");

        // for other player
        assertListener(
                "listener2-player1 created in field: [\n" +
                "    PMessage(id=4, text=message4, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12352)]");

        // when then
        // player1 create personal field message5 for itself
        with.time.nowIs(12353L);
        with.chat.check(
                chat(0).postFieldFor("player1", "message5", "room"),
                "[PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12353)]");

        // for itself
        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12353)]");

        // when then
        // player2 create personal field message6 for itself
        with.time.nowIs(12354L);
        with.chat.check(
                chat(1).postFieldFor("player2", "message6", "room"),
                "[PMessage(id=6, text=message6, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12354)]");

        // for itself
        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12354)]");

        // given
        Filter filter = Filter
                .room("room")
                .afterId(1)
                .beforeId(MAX)
                .inclusive(true)
                .count(MAX)
                .get();

        // when then
        // player1 get all field messages
        with.chat.check(
                chat(0).getAllField(filter),
                "[PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349), \n" +
                "PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12350), \n" +
                "PMessage(id=4, text=message4, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12352), \n" +
                "PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12353)]");

        assertListener(
                "listener1-player1 created in field: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349), \n" +
                "    PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12350), \n" +
                "    PMessage(id=4, text=message4, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12352), \n" +
                "    PMessage(id=5, text=message5, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12353)]");

        // when then
        // player2 get all field messages
        with.chat.check(
                chat(1).getAllField(filter),
                "[PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12349), \n" +
                "PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12350), \n" +
                "PMessage(id=3, text=message3, room=room, type=3, topicId=1, \n" +
                "    playerId=player1, playerName=player1-name, time=12351), \n" +
                "PMessage(id=6, text=message6, room=room, type=3, topicId=1, \n" +
                "    playerId=player2, playerName=player2-name, time=12354)]");

        assertListener(
                "listener2-player2 created in field: [\n" +
                "    PMessage(id=1, text=message1, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12349), \n" +
                "    PMessage(id=2, text=message2, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12350), \n" +
                "    PMessage(id=3, text=message3, room=room, type=3, topicId=1, \n" +
                "        playerId=player1, playerName=player1-name, time=12351), \n" +
                "    PMessage(id=6, text=message6, room=room, type=3, topicId=1, \n" +
                "        playerId=player2, playerName=player2-name, time=12354)]");
    }

    private ChatAuthority chat(int index) {
        return chats.get(index);
    }

    private void givenThreePlayers() {
        createPlayerWithControl("player1", "room", "first");
        createPlayerWithControl("player2", "room", "first");
        createPlayerWithControl("player3", "room2", "first");
    }

    private Deal createPlayerWithControl(String player, String room, String game) {
        int index = listeners.size();
        Deal deal = with.login.register(player, "ip", room, game);
        with.login.join(player, room);
        listeners.add(getListener(index + 1));
        chats.add(service.authority(player, listeners.get(index)));

        assertListener("");
        chat.removeAll();
        return deal;
    }

    private void assertListener(String expected) {
        assertEquals(expected,
                split(logs.stream().collect(joining(",\n")),
                        ", \n\t\tplayerId",
                        "\n\tPMessage(",
                        ", \nlistener"));
        logs.clear();
    }

    private OnChange getListener(int id) {
        return new OnChange() {
            @Override
            public void deleted(List<PMessage> messages, ChatType type, String playerId) {
                logs.add(String.format("listener%s-%s deleted in %s: %s",
                        id, playerId, type.name().toLowerCase(), messages));
            }

            @Override
            public void created(List<PMessage> messages, ChatType type, String playerId) {
                logs.add(String.format("listener%s-%s created in %s: %s",
                        id, playerId, type.name().toLowerCase(), messages));
            }
        };
    }
}