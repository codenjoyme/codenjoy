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
import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.ChatType;
import static com.codenjoy.dojo.services.chat.ChatType.*;

import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.ChatTest;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.stuff.SmartAssert;
import com.codenjoy.dojo.web.rest.TestLogin;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
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

    protected TestLogin login;

    @SpyBean
    private FieldService fields;

    private List<Chat.Message> messages = new LinkedList<>();

    public Chat.Message addMessage(String room, String player, Integer topicId, ChatType type) {
        return ChatTest.addMessage(chat, messages, room, player, topicId, type);
    }

    @Before
    public void setup() {
        login = new TestLogin(config, players, registration, deals);

        chat.removeAll();
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
        addMessage("room1", "player1", null, ROOM); // 1
        addMessage("room2", "player2", null, ROOM); // 2
        addMessage("room1", "player3", null, ROOM); // 3  last room1
        addMessage("room2", "player2", null, ROOM); // 4  last room2
        addMessage("room1", "player1", 1, TOPIC);   // 5
        addMessage("room2", "player2", 2, TOPIC);   // 6
        addMessage("room1", "player1", 1, TOPIC);   // 7  last topic1
        addMessage("room2", "player2", 2, TOPIC);   // 8
        addMessage("room1", "player2", 4, TOPIC);   // 9
        addMessage("room2", "player3", 3, TOPIC);   // 10 last topic3
        addMessage("room2", "player2", 2, TOPIC);   // 11 last topic2
        addMessage("room1", "player2", 4, TOPIC);   // 12 last topic4
        addMessage("room1", "player1", 1, FIELD);   // 13
        addMessage("room1", "player1", 1, FIELD);   // 14
        addMessage("room2", "player3", 3, FIELD);   // 15
        addMessage("room1", "player1", 1, FIELD);   // 16 last field1
        addMessage("room1", "player2", 4, FIELD);   // 17
        addMessage("room2", "player2", 2, FIELD);   // 18 last field2
        addMessage("room2", "player3", 3, FIELD);   // 19 last field3
        addMessage("room1", "player2", 4, FIELD);   // 20 last field4

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
        login.register("player1", "ip", "name", "first");
        login.join("player1", "room");

        login.register("player2", "ip", "name", "first");
        login.join("player2", "room");

        // when
        ChatControl player1 = service.control("player1");
        ChatControl player2 = service.control("player2");

        // when then
        nowIs(12345L);
        assertEquals("PMessage(id=1, text=message1, room=room, type=1, topicId=null, " +
                        "playerId=player1, playerName=player1-name, time=12345)",
                player1.postRoom("message1", "room").toString()); // 1

        nowIs(12346L);
        assertEquals("PMessage(id=2, text=message2, room=room, type=1, topicId=null, " +
                        "playerId=player2, playerName=player2-name, time=12346)",
                player2.postRoom("message2", "room").toString()); // 2

        nowIs(12347L);
        assertEquals("PMessage(id=3, text=message3, room=room, type=2, topicId=1, " +
                        "playerId=player1, playerName=player1-name, time=12347)",
                player1.postTopic(1, "message3", "room").toString()); // 3

        nowIs(12348L);
        assertEquals("PMessage(id=4, text=message4, room=room, type=2, topicId=2, " +
                        "playerId=player2, playerName=player2-name, time=12348)",
                player2.postTopic(2, "message4", "room").toString()); // 4

        nowIs(12349L);
        assertEquals("PMessage(id=5, text=message5, room=room, type=3, topicId=2, " +
                        "playerId=player1, playerName=player1-name, time=12349)",
                player1.postField("message5", "room").toString()); // 5

        nowIs(12350L);
        assertEquals("PMessage(id=6, text=message6, room=room, type=3, topicId=4, " +
                        "playerId=player2, playerName=player2-name, time=12350)",
                player2.postField("message6", "room").toString()); // 6

        nowIs(12351L);
        assertEquals("PMessage(id=7, text=message7, room=room, type=3, topicId=2, " +
                        "playerId=player1, playerName=player1-name, time=12351)",
                player1.postField("message7", "room").toString()); // 7

        nowIs(12352L);
        assertEquals("PMessage(id=8, text=message8, room=room, type=3, topicId=4, " +
                        "playerId=player2, playerName=player2-name, time=12352)",
                player2.postField("message8", "room").toString()); // 8

        assertEquals("PMessage(id=1, text=message1, room=room, type=1, topicId=null, " +
                        "playerId=player1, playerName=player1-name, time=12345)",
                player1.get(1, "room").toString());

        assertEquals("PMessage(id=5, text=message5, room=room, type=3, topicId=2, " +
                        "playerId=player1, playerName=player1-name, time=12349)",
                player2.get(5, "room").toString());

        Filter filter = Filter
                .room("room")
                .afterId(1)
                .beforeId(10)
                .inclusive(true)
                .count(10)
                .get();

        assertEquals("[PMessage(id=1, text=message1, room=room, type=1, topicId=null, " +
                        "playerId=player1, playerName=player1-name, time=12345), " +
                        "PMessage(id=2, text=message2, room=room, type=1, topicId=null, " +
                        "playerId=player2, playerName=player2-name, time=12346)]",
                player1.getAllRoom(filter).toString());

        assertEquals("[PMessage(id=1, text=message1, room=room, type=1, topicId=null, " +
                        "playerId=player1, playerName=player1-name, time=12345), " +
                        "PMessage(id=2, text=message2, room=room, type=1, topicId=null, " +
                        "playerId=player2, playerName=player2-name, time=12346)]",
                player2.getAllRoom(filter).toString());

        assertEquals("[PMessage(id=3, text=message3, room=room, type=2, topicId=1, " +
                        "playerId=player1, playerName=player1-name, time=12347)]",
                player1.getAllTopic(1, filter).toString());

        assertEquals("[PMessage(id=4, text=message4, room=room, type=2, topicId=2, " +
                        "playerId=player2, playerName=player2-name, time=12348)]",
                player2.getAllTopic(2, filter).toString());

        assertEquals("[PMessage(id=5, text=message5, room=room, type=3, topicId=2, " +
                        "playerId=player1, playerName=player1-name, time=12349), " +
                        "PMessage(id=7, text=message7, room=room, type=3, topicId=2, " +
                        "playerId=player1, playerName=player1-name, time=12351)]",
                player1.getAllField(filter).toString());

        assertEquals("[PMessage(id=6, text=message6, room=room, type=3, topicId=4, " +
                        "playerId=player2, playerName=player2-name, time=12350), " +
                        "PMessage(id=8, text=message8, room=room, type=3, topicId=4, " +
                        "playerId=player2, playerName=player2-name, time=12352)]",
                player2.getAllField(filter).toString());

        assertEquals(true,
                player1.delete(5, "room"));

        assertEquals(true,
                player2.delete(6, "room"));

        assertEquals("[PMessage(id=7, text=message7, room=room, type=3, topicId=2, " +
                        "playerId=player1, playerName=player1-name, time=12351)]",
                player1.getAllField(filter).toString());

        assertEquals("[PMessage(id=8, text=message8, room=room, type=3, topicId=4, " +
                        "playerId=player2, playerName=player2-name, time=12352)]",
                player2.getAllField(filter).toString());

    }
}