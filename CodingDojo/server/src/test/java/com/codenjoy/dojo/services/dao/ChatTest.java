package com.codenjoy.dojo.services.dao;

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

import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.helper.ChatHelper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import com.codenjoy.dojo.stuff.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static com.codenjoy.dojo.services.chat.ChatType.*;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;

// TODO try @SpringBootTest
public class ChatTest {

    public static final int MAX = 100;

    private Chat chat;
    private ChatHelper messages;

    @Before
    public void setup() {
        String dbFile = "target/chat.db" + new Random().nextInt();
        chat = new Chat(
                new SqliteConnectionThreadPoolFactory(false, dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }));
        
        messages = new ChatHelper(chat, null);
    }

    @After
    public void tearDown() {
        chat.removeDatabase();
        SmartAssert.checkResult();
    }

    @Test
    public void shouldGenerateId_whenSaveMessage() {
        // when
        Chat.Message message1 = messages.post("room1", "player1");

        // then
        assertEquals("Chat.Message(id=1, topicId=null, type=ROOM(1), room=room1, playerId=player1, recipientId=null, time=1615231523345, text=message1)",
                message1.toString());

        // when
        Chat.Message message2 = messages.post("room2", "player2");

        // then
        assertEquals("Chat.Message(id=2, topicId=null, type=ROOM(1), room=room2, playerId=player2, recipientId=null, time=1615231623345, text=message2)",
                message2.toString());
    }

    @Test
    public void shouldGetTypeById_allCases() {
        // given
        messages.post("room1", "player1");                 // id = 1
        messages.post("room1", "player1", 1, ROOM_TOPIC);  // id = 2
        messages.post("room1", "player1", 1, FIELD);       // id = 3
        messages.post("room1", "player1", 3, FIELD_TOPIC); // id = 4

        // when then
        assertGetTypeById();
    }

    private void assertGetTypeById() {
        assertEquals("ROOM(1)", chat.getTypeById(1).toString());
        assertEquals("ROOM_TOPIC(2)", chat.getTypeById(2).toString());
        assertEquals("FIELD(3)", chat.getTypeById(3).toString());
        assertEquals("FIELD_TOPIC(4)", chat.getTypeById(4).toString());
        assertEquals(null, chat.getTypeById(5));
    }

    @Test
    public void shouldGetTypeById_allCases_doNotAffectWhenRemove() {
        // given
        shouldGetTypeById_allCases();

        // when
        assertEquals(true, chat.deleteMessage("room1", 1, "player1"));
        assertEquals(true, chat.deleteMessage("room1", 2, "player1"));
        assertEquals(true, chat.deleteMessage("room1", 3, "player1"));
        assertEquals(true, chat.deleteMessage("room1", 4, "player1"));
        assertEquals(false, chat.deleteMessage("room1", 5, "player1")); // not exists

        // then
        assertGetTypeById();
    }

    @Test
    public void shouldGetLastMessageId_whenAddedTopicMessages() {
        // when then
        assertEquals(null, chat.getLastMessageId("room1"));

        // when then
        messages.post("room1", "player1"); // id = 1

        assertEquals(Integer.valueOf(1),
                chat.getLastMessageId("room1"));

        // when then
        messages.post("room1", "player1", 1, ROOM_TOPIC); // id = 2

        assertEquals(Integer.valueOf(1),
                chat.getLastMessageId("room1"));

        // when then
        messages.post("room1", "player1", 1, ROOM_TOPIC); // id = 3

        assertEquals(Integer.valueOf(1),
                chat.getLastMessageId("room1"));
    }

    @Test
    public void shouldGetLastRoomMessageIds_whenAddedTopicMessages() {
        // when then
        assertEquals("{}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player1"); // id = 1

        assertEquals("{room1=1}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player1", 1, ROOM_TOPIC); // id = 2

        assertEquals("{room1=1}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player1", 1, ROOM_TOPIC); // id = 3

        assertEquals("{room1=1}",
                chat.getLastRoomMessageIds().toString());
    }

    @Test
    public void shouldGetLastRoomMessageIds() {
        // when then
        assertEquals("{}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player1"); // id = 1

        assertEquals("{room1=1}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player1"); // id = 2

        assertEquals("{room1=2}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player2"); // id = 3

        assertEquals("{room1=3}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room2", "player2"); // id = 4

        assertEquals("{room1=3, room2=4}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room1", "player2"); // id = 5

        assertEquals("{room2=4, room1=5}",
                chat.getLastRoomMessageIds().toString());

        // when then
        messages.post("room3", "player1"); // id = 6

        assertEquals("{room2=4, room1=5, room3=6}",
                chat.getLastRoomMessageIds().toString());

        // when then
        chat.deleteMessage("room3", 6, "player1");

        assertEquals("{room2=4, room1=5}",
                chat.getLastRoomMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 5, "player2");

        assertEquals("{room1=3, room2=4}",
                chat.getLastRoomMessageIds().toString());

        // when then
        chat.deleteMessage("room2", 4, "player2");

        assertEquals("{room1=3}",
                chat.getLastRoomMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 3, "player2");

        assertEquals("{room1=2}",
                chat.getLastRoomMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 2, "player1");

        assertEquals("{room1=1}",
                chat.getLastRoomMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 1, "player1");

        assertEquals("{}",
                chat.getLastRoomMessageIds().toString());
    }

    @Test
    public void shouldGetLastTopicMessageIds() {
        // when then
        messages.post("room1", "player1"); // id = 1
        messages.post("room1", "player2"); // id = 2

        assertEquals("{}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        messages.post("room1", "player1", 1, ROOM_TOPIC); // id = 3

        assertEquals("{1=3}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        messages.post("room1", "player2", 2, ROOM_TOPIC); // id = 4

        assertEquals("{1=3, 2=4}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        messages.post("room2", "player2", 1, ROOM_TOPIC); // id = 5

        assertEquals("{2=4, 1=5}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        messages.post("room1", "player2", 2, ROOM_TOPIC); // id = 6

        assertEquals("{1=5, 2=6}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        messages.post("room3", "player1", 1, ROOM_TOPIC); // id = 7

        assertEquals("{2=6, 1=7}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room3", 7, "player1");

        assertEquals("{1=5, 2=6}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room1", 6, "player2");

        assertEquals("{2=4, 1=5}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room2", 5, "player2");

        assertEquals("{1=3, 2=4}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room1", 4, "player2");

        assertEquals("{1=3}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room1", 3, "player1");

        assertEquals("{}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room1", 2, "player1");

        assertEquals("{}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());

        // when then
        chat.deleteMessage("room1", 1, "player1");

        assertEquals("{}",
                chat.getLastTopicMessageIds(ROOM_TOPIC).toString());
    }

    @Test
    public void shouldGetLastMessageId() {
        // when then
        assertEquals(null, chat.getLastMessageId("room1"));

        // when then
        messages.post("room1", "player1"); // id = 1

        assertEquals(Integer.valueOf(1),
                chat.getLastMessageId("room1"));

        // when then
        messages.post("room1", "player1"); // id = 2

        assertEquals(Integer.valueOf(2),
                chat.getLastMessageId("room1"));

        // when then
        messages.post("room1", "player2"); // id = 3

        assertEquals(Integer.valueOf(3),
                chat.getLastMessageId("room1"));
        assertEquals(null,
                chat.getLastMessageId("room2"));

        // when then
        messages.post("room2", "player2"); // id = 4 // не включен - другая комната

        assertEquals(Integer.valueOf(3),
                chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4),
                chat.getLastMessageId("room2"));

        // when then
        messages.post("room1", "player2"); // id = 5

        assertEquals(Integer.valueOf(5),
                chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4),
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));

        // when then
        messages.post("room3", "player1"); // id = 6

        assertEquals(Integer.valueOf(5),
                chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4),
                chat.getLastMessageId("room2"));
        assertEquals(Integer.valueOf(6),
                chat.getLastMessageId("room3"));

        // when then
        chat.deleteMessage("room3", 6, "player1");

        assertEquals(Integer.valueOf(5),
                chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4),
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));

        // when then
        chat.deleteMessage("room1", 5, "player2");

        assertEquals(Integer.valueOf(3),
                chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4),
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));

        // when then
        chat.deleteMessage("room2", 4, "player2");

        assertEquals(Integer.valueOf(3),
                chat.getLastMessageId("room1"));
        assertEquals(null,
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));

        // when then
        chat.deleteMessage("room1", 3, "player2");

        assertEquals(Integer.valueOf(2),
                chat.getLastMessageId("room1"));
        assertEquals(null,
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));

        // when then
        chat.deleteMessage("room1", 2, "player1");

        assertEquals(Integer.valueOf(1),
                chat.getLastMessageId("room1"));
        assertEquals(null,
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));

        // when then
        chat.deleteMessage("room1", 1, "player1");

        assertEquals(null,
                chat.getLastMessageId("room1"));
        assertEquals(null,
                chat.getLastMessageId("room2"));
        assertEquals(null,
                chat.getLastMessageId("room3"));
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedMoreThanPresent() {
        // given
        messages.post("room1", "player1"); // id = 1
        messages.post("room1", "player1"); // id = 2
        messages.post("room1", "player2"); // id = 3
        messages.post("room2", "player2"); // id = 4 // не включен - другая комната

        // when then
        messages.assertThat(1, 2, 3)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room1")
                                .count(10)
                                .get()));
    }

    public static String toString(List<Chat.Message> messages) {
        return messages.toString()
                .replace("), Chat.Message(",
                        "), \nChat.Message(");
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedLessThanPresent() {
        // given
        // так как getMessages берет только последние сообщения
        // это сообщение не включено - запросили не так много сообщений
        messages.post("room1", "player1");
        messages.post("room1", "player1");
        messages.post("room1", "player2");
        messages.post("room2", "player2"); // не включен - другая комната

        // when then
        messages.assertThat(2, 3)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room1")
                                .count(2)
                                .get()));
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenNoSuchRoom() {
        // given
        messages.post("room1", "player1");
        
        // when then
        messages.assertThat()
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room2")
                                .count(2)
                                .get()));
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenZeroMessages() {
        // given
        messages.post("room1", "player1");

        // when then
        messages.assertThat()
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room1")
                                .count(0)
                                .get()));
    }

    @Test
    public void shouldGetMessageById() {
        // given
        messages.post("room1", "player1");                 // 1
        messages.post("room1", "player1", 1, ROOM_TOPIC);  // 2
        messages.post("room1", "player2", 1, FIELD);       // 3
        messages.post("room1", "player2", 3, FIELD_TOPIC); // 4
        messages.post("room2", "player2");                 // 5

        // when then
        assertMessages();
    }

    static class Triplet {
        int roomTopic;
        int fieldTopic;
        int field;
    }

    @Test
    public void shouldGetAllMessages_casePersonalMessages() {
        // given
        // for all
        Triplet ids = givenAllTypesMessagesWithPersonal();

        // when then
        // for room
        messages.assertThat(1)
                .in(chat.getMessages(ROOM, null,
                        filter("room1", null)));

        messages.assertThat(1, 6)
                .in(chat.getMessages(ROOM, null,
                        filter("room1", "player1")));

        messages.assertThat(1, 11)
                .in(chat.getMessages(ROOM, null,
                        filter("room1", "player2")));

        // for room topic
        messages.assertThat(2)
                .in(chat.getMessages(ROOM_TOPIC, ids.roomTopic,
                        filter("room1", null)));

        messages.assertThat(2, 7)
                .in(chat.getMessages(ROOM_TOPIC, ids.roomTopic,
                        filter("room1", "player1")));

        messages.assertThat(2, 12)
                .in(chat.getMessages(ROOM_TOPIC, ids.roomTopic,
                        filter("room1", "player2")));

        // for field
        messages.assertThat(3)
                .in(chat.getMessages(FIELD, ids.field,
                        filter("room1", null)));

        messages.assertThat(3, 8)
                .in(chat.getMessages(FIELD, ids.field,
                        filter("room1", "player1")));

        messages.assertThat(3, 13)
                .in(chat.getMessages(FIELD, ids.field,
                        filter("room1", "player2")));

        // for field topic
        messages.assertThat(4)
                .in(chat.getMessages(FIELD_TOPIC, ids.fieldTopic,
                        filter("room1", null)));

        messages.assertThat(4, 9)
                .in(chat.getMessages(FIELD_TOPIC, ids.fieldTopic,
                        filter("room1", "player1")));

        messages.assertThat(4, 14)
                .in(chat.getMessages(FIELD_TOPIC, ids.fieldTopic,
                        filter("room1", "player2")));
    }

    private Triplet givenAllTypesMessagesWithPersonal() {
        Triplet ids = new Triplet();

        // 1
        ids.roomTopic = messages.post("room1", "player1", null, ROOM).getId();
        // 2
        messages.post("room1", "player1", ids.roomTopic, ROOM_TOPIC);
        // 3
        ids.field = 1;
        ids.fieldTopic = messages.post("room1", "player2", ids.field, FIELD).getId();
        // 4
        messages.post("room1", "player2", ids.fieldTopic, FIELD_TOPIC);
        // 5
        messages.post("room2", "player2", null, ROOM);

        // personal for player1
        messages.post("room1", "player1", null, ROOM, "player1");                  // 6
        messages.post("room1", "player1", ids.roomTopic, ROOM_TOPIC, "player1");   // 7
        messages.post("room1", "player2", ids.field, FIELD, "player1");            // 8
        messages.post("room1", "player2", ids.fieldTopic, FIELD_TOPIC, "player1"); // 9
        messages.post("room2", "player2", null, ROOM, "player1");                  // 10

        // personal for player2
        messages.post("room1", "player1", null, ROOM, "player2");                  // 11
        messages.post("room1", "player1", ids.roomTopic, ROOM_TOPIC, "player2");   // 12
        messages.post("room1", "player2", ids.field, FIELD, "player2");            // 13
        messages.post("room1", "player2", ids.fieldTopic, FIELD_TOPIC, "player2"); // 14
        messages.post("room2", "player2", null, ROOM, "player2");                  // 15

        return ids;
    }

    @Test
    public void shouldGetMessagesBetween_casePersonalMessages() {
        // given
        Triplet ids = givenAllTypesMessagesWithPersonal();
        
        // when then
        // for room
        messages.assertThat(1)
                .in(chat.getMessagesBetween(ROOM, null,
                        filter("room1", null)));

        messages.assertThat(1, 6)
                .in(chat.getMessagesBetween(ROOM, null,
                        filter("room1", "player1")));

        messages.assertThat(1, 11)
                .in(chat.getMessagesBetween(ROOM, null,
                        filter("room1", "player2")));

        // for room topic
        messages.assertThat(2)
                .in(chat.getMessagesBetween(ROOM_TOPIC, ids.roomTopic,
                        filter("room1", null)));

        messages.assertThat(2, 7)
                .in(chat.getMessagesBetween(ROOM_TOPIC, ids.roomTopic,
                        filter("room1", "player1")));

        messages.assertThat(2, 12)
                .in(chat.getMessagesBetween(ROOM_TOPIC, ids.roomTopic,
                        filter("room1", "player2")));

        // for field
        messages.assertThat(3)
                .in(chat.getMessagesBetween(FIELD, ids.field,
                        filter("room1", null)));

        messages.assertThat(3, 8)
                .in(chat.getMessagesBetween(FIELD, ids.field,
                        filter("room1", "player1")));

        messages.assertThat(3, 13)
                .in(chat.getMessagesBetween(FIELD, ids.field,
                        filter("room1", "player2")));

        // for field topic
        messages.assertThat(4)
                .in(chat.getMessagesBetween(FIELD_TOPIC, ids.fieldTopic,
                        filter("room1", null)));

        messages.assertThat(4, 9)
                .in(chat.getMessagesBetween(FIELD_TOPIC, ids.fieldTopic,
                        filter("room1", "player1")));

        messages.assertThat(4, 14)
                .in(chat.getMessagesBetween(FIELD_TOPIC, ids.fieldTopic,
                        filter("room1", "player2")));
    }

    private Filter filter(String room, String recipientId) {
        return Filter.room(room)
                .count(MAX)
                .afterId(0)
                .beforeId(MAX)
                .inclusive(true)
                .recipientId(recipientId)
                .get();
    }

    private void assertMessages() {
        assertEquals("Chat.Message(id=1, topicId=null, type=ROOM(1), room=room1, playerId=player1, recipientId=null, time=1615231523345, text=message1)",
                chat.getMessageById(1).toString());

        assertEquals("Chat.Message(id=2, topicId=1, type=ROOM_TOPIC(2), room=room1, playerId=player1, recipientId=null, time=1615231623345, text=message2)",
                chat.getMessageById(2).toString());

        assertEquals("Chat.Message(id=3, topicId=1, type=FIELD(3), room=room1, playerId=player2, recipientId=null, time=1615231723345, text=message3)",
                chat.getMessageById(3).toString());

        assertEquals("Chat.Message(id=4, topicId=3, type=FIELD_TOPIC(4), room=room1, playerId=player2, recipientId=null, time=1615231823345, text=message4)",
                chat.getMessageById(4).toString());

        assertEquals("Chat.Message(id=5, topicId=null, type=ROOM(1), room=room2, playerId=player2, recipientId=null, time=1615231923345, text=message5)",
                chat.getMessageById(5).toString());
    }

    private void assertAnyMessages() {
        assertEquals("Chat.Message(id=1, topicId=null, type=ROOM(1), room=room1, playerId=player1, recipientId=null, time=1615231523345, text=message1)",
                chat.getAnyMessageById(1).toString());

        assertEquals("Chat.Message(id=2, topicId=1, type=ROOM_TOPIC(2), room=room1, playerId=player1, recipientId=null, time=1615231623345, text=message2)",
                chat.getAnyMessageById(2).toString());

        assertEquals("Chat.Message(id=3, topicId=1, type=FIELD(3), room=room1, playerId=player2, recipientId=null, time=1615231723345, text=message3)",
                chat.getAnyMessageById(3).toString());

        assertEquals("Chat.Message(id=4, topicId=3, type=FIELD_TOPIC(4), room=room1, playerId=player2, recipientId=null, time=1615231823345, text=message4)",
                chat.getAnyMessageById(4).toString());

        assertEquals("Chat.Message(id=5, topicId=null, type=ROOM(1), room=room2, playerId=player2, recipientId=null, time=1615231923345, text=message5)",
                chat.getAnyMessageById(5).toString());
    }

    @Test
    public void shouldGetAnyMessageById() {
        // given
        messages.post("room1", "player1");                 // 1
        messages.post("room1", "player1", 1, ROOM_TOPIC);  // 2
        messages.post("room1", "player2", 1, FIELD);       // 3
        messages.post("room1", "player2", 3, FIELD_TOPIC); // 4
        messages.post("room2", "player2");                 // 5

        // when then
        assertAnyMessages();
    }

    @Test
    public void shouldGetAnyMessageById_affectWhenRemove() {
        // given
        shouldGetAnyMessageById();

        // when
        chat.deleteMessage("room1", 1, "player1");
        chat.deleteMessage("room1", 2, "player1");
        chat.deleteMessage("room1", 3, "player2");
        chat.deleteMessage("room2", 4, "player2");

        // then
        assertAnyMessages();
    }

    @Test
    public void shouldGetMessageById_whenNotExists() {
        // when then
        assertEquals(null,
                chat.getMessageById(100500));
    }

    @Test
    public void shouldDeleteMessageById() {
        // given
        messages.post("room", "player1"); // id = 1
        messages.post("room", "player1"); // id = 2
        messages.post("room", "player2"); // id = 3
        messages.post("room", "player3"); // id = 4

        // when then
        messages.assertThat(1, 2, 3, 4)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when
        assertEquals(true, chat.deleteMessage("room", 1, "player1"));

        // then
        messages.assertThat(2, 3, 4)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when
        assertEquals(true, chat.deleteMessage("room", 3, "player2"));

        // then
        messages.assertThat(2, 4)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when
        assertEquals(true, chat.deleteMessage("room", 4, "player3"));

        // then
        messages.assertThat(2)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));
    }

    @Test
    public void shouldDeleteMessageById_whenRoomNotExists() {
        // given
        messages.post("room", "player"); // id = 1

        // when then
        assertEquals(false, chat.deleteMessage("otherRoom", 1, "player"));
    }

    @Test
    public void shouldDeleteMessageById_whenInvalidRoom() {
        // given
        messages.post("room", "player"); // id = 1

        // when then
        assertEquals(false, chat.deleteMessage("room", 100500, "player"));
    }

    @Test
    public void shouldDeleteMessageById_whenOtherPlayer() {
        // given
        messages.post("room", "player");

        // then
        messages.assertThat(1)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when then
        assertEquals(false, chat.deleteMessage("room", 1, "otherPlayer"));
    }

    @Test
    public void shouldGetMessagesAfterId() {
        // given
        givenCase();

        // when then
        // первое сообщение c id = afterId не включается
        messages.assertThat(3, 5, 7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(1)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3, 5, 7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(1)
                                .inclusive(true)
                                .get()));

        // берутся только два сверху, хотя доступные 3
        messages.assertThat(3, 5)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(2)
                                .afterId(1)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(2)
                                .afterId(1)
                                .inclusive(true)
                                .get()));

        // можно указывать даже айдишку из другого чата - они порядковые
        messages.assertThat(3, 5, 7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(2)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(3, 5, 7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(2)
                                .inclusive(true)
                                .get()));

        // минус одно сообщение с id = afterId
        messages.assertThat(5, 7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(4)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(5, 7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(4)
                                .inclusive(true)
                                .get()));

        // минус одно сообщение с id = afterId
        messages.assertThat()
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(7)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(7)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(7)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetMessagesAfterId_caseTopic() {
        // given
        givenCase_topic();

        // 1 - первое топик сообщение
        // 2 - второе топик сообщение
        // 3 - первое топик сообщение из другой комнаты

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        messages.assertThat(4, 7, 10, 13)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(1)
                                .inclusive(false)
                                .get()));

        messages.assertThat(4, 7)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .afterId(1)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5, 8, 11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(1)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5, 8)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(2)
                                .afterId(1)
                                .inclusive(false)
                                .get()));

        // сообщение c id = afterId, и id = beforeId не включается
        messages.assertThat(7, 10, 13)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(4)
                                .inclusive(false)
                                .get()));

        messages.assertThat(7, 10)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .afterId(4)
                                .inclusive(false)
                                .get()));

        messages.assertThat(8, 11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(5)
                                .inclusive(false)
                                .get()));

        messages.assertThat(8, 11)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(2)
                                .afterId(5)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(4, 7, 10, 13)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(4)
                                .inclusive(true)
                                .get()));

        messages.assertThat(4, 7)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .afterId(4)
                                .inclusive(true)
                                .get()));

        messages.assertThat(5, 8, 11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(5)
                                .inclusive(true)
                                .get()));

        messages.assertThat(5, 8)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(2)
                                .afterId(5)
                                .inclusive(true)
                                .get()));

        // когда встретились на границе
        messages.assertThat(10, 13)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(7)
                                .inclusive(false)
                                .get()));

        messages.assertThat(10)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(1)
                                .afterId(7)
                                .inclusive(false)
                                .get()));

        messages.assertThat(11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(8)
                                .inclusive(false)
                                .get()));

        messages.assertThat(11)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(1)
                                .afterId(8)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(7, 10, 13)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(7)
                                .inclusive(true)
                                .get()));

        messages.assertThat(7)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(1)
                                .afterId(7)
                                .inclusive(true)
                                .get()));

        messages.assertThat(8, 11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .afterId(8)
                                .inclusive(true)
                                .get()));

        messages.assertThat(8)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(1)
                                .afterId(8)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetMessagesBeforeId() {
        // given
        givenCase();

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        // TODO Поганий стиль програмування передавати null аргумент.
        //      Варто подумати над двома методами getTopicMessagesBefore()
        //      & getFieldMessagesBefore().
        //      Думал об этом, но все дело в том что там есть 4 метода:
        //      before, after, between, all. И теперь получается надо
        //      сделать им синонимы. А потом еще для field 4 таких же.
        //      Решил не делать этого. Chat DAO – это универсальное
        //      низкоуровневое апи, я хочу его сделать независимым от
        //      типа сообщений.
        messages.assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(8)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));

        // первое сообщение c id = beforeId не включается
        messages.assertThat(1, 3, 5)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(7)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        // берутся только два но с конца, хотя доступные 3
        messages.assertThat(3, 5)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(2)
                                .beforeId(7)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(5, 7)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(2)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        // минус одно сообщение с id = beforeId
        messages.assertThat(1, 3)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(5)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3, 5)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(5)
                                .inclusive(true)
                                .get()));

        // минус одно сообщение с id = beforeId
        messages.assertThat()
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(1)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(1)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetMessagesBeforeId_caseTopic() {
        // given
        givenCase_topic();

        // 1 - первое топик сообщение
        // 2 - второе топик сообщение
        // 3 - первое топик сообщение из другой комнаты

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        messages.assertThat(4, 7, 10, 13)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(15)
                                .inclusive(false)
                                .get()));

        messages.assertThat(10, 13)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .beforeId(15)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5, 8, 11, 14)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(15)
                                .inclusive(false)
                                .get()));

        messages.assertThat(11, 14)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(2)
                                .beforeId(15)
                                .inclusive(false)
                                .get()));

        // сообщение c id = afterId, и id = beforeId не включается
        messages.assertThat(4, 7, 10)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(13)
                                .inclusive(false)
                                .get()));

        messages.assertThat(7, 10)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .beforeId(13)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5, 8, 11)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(14)
                                .inclusive(false)
                                .get()));

        messages.assertThat(8, 11)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(2)
                                .beforeId(14)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(4, 7, 10, 13)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(13)
                                .inclusive(true)
                                .get()));

        messages.assertThat(10, 13)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .beforeId(13)
                                .inclusive(true)
                                .get()));

        messages.assertThat(5, 8, 11, 14)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        messages.assertThat(11, 14)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(2)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // когда встретились на границе
        messages.assertThat(4)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(7)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(8)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(4, 7)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        messages.assertThat(7)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(1)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        messages.assertThat(5, 8)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));

        messages.assertThat(8)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(1)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetMessagesBetweenIds() {
        // given
        givenCase();

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        // но первое сообщение c id = afterId не включается
        messages.assertThat(3, 5, 7)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(8)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));

        // первое сообщение c id = beforeId не включается
        // так же как и последнее сообщение c id = afterId тоже не включается
        messages.assertThat(3, 5)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(7)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        // минус одно сообщение с id = beforeId
        // минус одно сообщение с id = afterId
        messages.assertThat(5)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(7)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(3, 5, 7)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        // когда встретились на границе
        messages.assertThat()
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(5)
                                .beforeId(5)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(5)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(5)
                                .beforeId(5)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetMessagesBetweenIds_caseTopic() {
        // given
        givenCase_topic();

        // 1 - первое топик сообщение
        // 2 - второе топик сообщение
        // 3 - первое топик сообщение из другой комнаты

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        messages.assertThat(4, 7, 10, 13)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(15)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5, 8, 11, 14)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(15)
                                .inclusive(false)
                                .get()));

        // сообщение c id = afterId, и id = beforeId не включается
        messages.assertThat(7, 10)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(4)
                                .beforeId(13)
                                .inclusive(false)
                                .get()));

        messages.assertThat(8, 11)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .afterId(5)
                                .beforeId(14)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(4, 7, 10, 13)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(4)
                                .beforeId(13)
                                .inclusive(true)
                                .get()));

        messages.assertThat(5, 8, 11, 14)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .afterId(5)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // когда встретились на границе
        messages.assertThat()
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(7)
                                .beforeId(7)
                                .inclusive(false)
                                .get()));

        messages.assertThat()
                .in(chat.getMessagesBetween(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .afterId(8)
                                .beforeId(8)
                                .inclusive(false)
                                .get()));

        // то же только c inclusive = true
        messages.assertThat(7)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(7)
                                .beforeId(7)
                                .inclusive(true)
                                .get()));

        messages.assertThat(8)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .afterId(8)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));
    }

    public void givenCase() {
        messages.post("room", "player1");          // id = 1
        messages.post("otherRoom", "otherPlayer"); // id = 2 // другой чат - не берем
        messages.post("room", "player1");          // id = 3
        messages.post("otherRoom", "otherPlayer"); // id = 4 // другой чат - не берем
        messages.post("room", "player2");          // id = 5
        messages.post("otherRoom", "otherPlayer"); // id = 6 // другой чат - не берем
        messages.post("room", "player2");          // id = 7
        messages.post("otherRoom", "otherPlayer"); // id = 8 // другой чат - не берем

        assertEquals("[Chat.Message(id=1, topicId=null, type=ROOM(1), room=room, playerId=player1, recipientId=null, time=1615231523345, text=message1), " +
                        "Chat.Message(id=3, topicId=null, type=ROOM(1), room=room, playerId=player1, recipientId=null, time=1615231723345, text=message3), " +
                        "Chat.Message(id=5, topicId=null, type=ROOM(1), room=room, playerId=player2, recipientId=null, time=1615231923345, text=message5), " +
                        "Chat.Message(id=7, topicId=null, type=ROOM(1), room=room, playerId=player2, recipientId=null, time=1615232123345, text=message7)]",
                chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .get()).toString());

        assertEquals("[Chat.Message(id=2, topicId=null, type=ROOM(1), room=otherRoom, playerId=otherPlayer, recipientId=null, time=1615231623345, text=message2), " +
                        "Chat.Message(id=4, topicId=null, type=ROOM(1), room=otherRoom, playerId=otherPlayer, recipientId=null, time=1615231823345, text=message4), " +
                        "Chat.Message(id=6, topicId=null, type=ROOM(1), room=otherRoom, playerId=otherPlayer, recipientId=null, time=1615232023345, text=message6), " +
                        "Chat.Message(id=8, topicId=null, type=ROOM(1), room=otherRoom, playerId=otherPlayer, recipientId=null, time=1615232223345, text=message8)]",
                chat.getMessages(ROOM, null,
                        Filter.room("otherRoom")
                                .count(MAX)
                                .get()).toString());
    }

    public void givenCase_topic() {
        messages.post("room", "player1");                         // id = 1  // первый topic
        messages.post("room", "player1");                         // id = 2  // второй topic
        messages.post("otherRoom", "otherPlayer");                // id = 3  // другой чат - не берем
        messages.post("room", "player1", 1, ROOM_TOPIC);          // id = 4
        messages.post("room", "player1", 2, ROOM_TOPIC);          // id = 5
        messages.post("otherRoom", "otherPlayer", 2, ROOM_TOPIC); // id = 6  // другой чат - не берем
        messages.post("room", "player2", 1, ROOM_TOPIC);          // id = 7
        messages.post("room", "player2", 2, ROOM_TOPIC);          // id = 8
        messages.post("otherRoom", "otherPlayer", 2, ROOM_TOPIC); // id = 9  // другой чат - не берем
        messages.post("room", "player2", 1, ROOM_TOPIC);          // id = 10
        messages.post("room", "player2", 2, ROOM_TOPIC);          // id = 11
        messages.post("otherRoom", "otherPlayer", 2, ROOM_TOPIC); // id = 12 // другой чат - не берем
        messages.post("room", "player2", 1, ROOM_TOPIC);          // id = 13
        messages.post("room", "player2", 2, ROOM_TOPIC);          // id = 14
        messages.post("otherRoom", "otherPlayer", 2, ROOM_TOPIC); // id = 15 // другой чат - не берем

        assertEquals("[Chat.Message(id=1, topicId=null, type=ROOM(1), room=room, playerId=player1, recipientId=null, time=1615231523345, text=message1), \n" +
                        "Chat.Message(id=2, topicId=null, type=ROOM(1), room=room, playerId=player1, recipientId=null, time=1615231623345, text=message2)]",
                toString(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(MAX)
                                .get())));

        assertEquals("[Chat.Message(id=4, topicId=1, type=ROOM_TOPIC(2), room=room, playerId=player1, recipientId=null, time=1615231823345, text=message4), \n" +
                        "Chat.Message(id=7, topicId=1, type=ROOM_TOPIC(2), room=room, playerId=player2, recipientId=null, time=1615232123345, text=message7), \n" +
                        "Chat.Message(id=10, topicId=1, type=ROOM_TOPIC(2), room=room, playerId=player2, recipientId=null, time=1615232423345, text=message10), \n" +
                        "Chat.Message(id=13, topicId=1, type=ROOM_TOPIC(2), room=room, playerId=player2, recipientId=null, time=1615232723345, text=message13)]",
                toString(chat.getMessages(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .get())));

        assertEquals("[Chat.Message(id=5, topicId=2, type=ROOM_TOPIC(2), room=room, playerId=player1, recipientId=null, time=1615231923345, text=message5), \n" +
                        "Chat.Message(id=8, topicId=2, type=ROOM_TOPIC(2), room=room, playerId=player2, recipientId=null, time=1615232223345, text=message8), \n" +
                        "Chat.Message(id=11, topicId=2, type=ROOM_TOPIC(2), room=room, playerId=player2, recipientId=null, time=1615232523345, text=message11), \n" +
                        "Chat.Message(id=14, topicId=2, type=ROOM_TOPIC(2), room=room, playerId=player2, recipientId=null, time=1615232823345, text=message14)]",
                toString(chat.getMessages(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .get())));

        assertEquals("[Chat.Message(id=3, topicId=null, type=ROOM(1), room=otherRoom, playerId=otherPlayer, recipientId=null, time=1615231723345, text=message3)]",
                toString(chat.getMessages(ROOM, null,
                        Filter.room("otherRoom")
                                .count(MAX)
                                .get())));

        assertEquals("[]",
                toString(chat.getMessages(ROOM_TOPIC, 3,
                        Filter.room("otherRoom")
                                .count(MAX)
                                .get())));
    }

    @Test
    public void shouldGetAllTopicMessages() {
        // given
        messages.post("room", "player");                // id = 1 // message in room, will be topic 1
        messages.post("room", "player");                // id = 2 // message in room, will be topic 2
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 3 // message for topic1
        messages.post("room", "player");                // id = 4 // just another one message in room
        messages.post("room", "player", 2, ROOM_TOPIC); // id = 5 // message for topic2
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 6 // message for topic1
        messages.post("room", "player");                // id = 7 // just another one message in room
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 8 // message for topic1

        // when then
        // all for room
        messages.assertThat(1, 2, 4, 7)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when then
        // all for topic 1 message in room
        messages.assertThat(3, 6, 8)
                .in(chat.getTopicMessages(ROOM_TOPIC, 1));

        // when then
        // all for topic 2 message in room
        messages.assertThat(5)
                .in(chat.getTopicMessages(ROOM_TOPIC, 2));

        // when then
        // get topic message like room message
        messages.assertThat()
                .in(chat.getTopicMessages(ROOM_TOPIC, 3));

        // when then
        // all for non topic message in room
        messages.assertThat()
                .in(chat.getTopicMessages(ROOM_TOPIC, 4));

        // when then
        // between messages in topic -> room messages between 3 ... 8
        messages.assertThat(4, 7)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(8)
                                .inclusive(false)
                                .get()));
    }

    @Test
    public void shouldGetMessages_affectWhenRemove() {
        // given
        givenMessages_affectWhenRemove();

        // when then
        // all for room
        messages.assertThat(1, 2, 7, 13)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when then
        // all for topic
        messages.assertThat(3, 4, 11, 14)
                .in(chat.getMessages(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // all for field
        messages.assertThat(5, 6, 12)
                .in(chat.getMessages(FIELD, 1,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // all for field
        messages.assertThat(9)
                .in(chat.getMessages(FIELD, 2,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when
        removeSeveralMessages_affectWhenRemove();

        // when then
        // all for room
        messages.assertThat(1, 7, 13)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when then
        // all for topic
        messages.assertThat(3, 11, 14)
                .in(chat.getMessages(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // all for field
        messages.assertThat(5, 12)
                .in(chat.getMessages(FIELD, 1,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // all for field
        messages.assertThat()
                .in(chat.getMessages(FIELD, 2,
                        Filter.room("room")
                                .count(10)
                                .get()));
    }

    @Test
    public void shouldGetById_affectWhenRemove() {
        // given
        givenMessages_affectWhenRemove();

        assertEquals("Chat.Message(id=2, topicId=null, type=ROOM(1), room=room, playerId=player, recipientId=null, time=1615231623345, text=message2)",
                chat.getMessageById(2).toString());

        assertEquals("Chat.Message(id=4, topicId=1, type=ROOM_TOPIC(2), room=room, playerId=player, recipientId=null, time=1615231823345, text=message4)",
                chat.getMessageById(4).toString());

        assertEquals("Chat.Message(id=6, topicId=1, type=FIELD(3), room=room, playerId=player, recipientId=null, time=1615232023345, text=message6)",
                chat.getMessageById(6).toString());

        assertEquals("Chat.Message(id=9, topicId=2, type=FIELD(3), room=room, playerId=player, recipientId=null, time=1615232323345, text=message9)",
                chat.getMessageById(9).toString());

        assertEquals("Chat.Message(id=10, topicId=2, type=ROOM_TOPIC(2), room=room, playerId=player, recipientId=null, time=1615232423345, text=message10)",
                chat.getMessageById(10).toString());

        // when
        removeSeveralMessages_affectWhenRemove();

        assertEquals(null, chat.getMessageById(2));
        assertEquals(null, chat.getMessageById(4));
        assertEquals(null, chat.getMessageById(6));
        assertEquals(null, chat.getMessageById(9));
        assertEquals(null, chat.getMessageById(10));
    }

    @Test
    public void shouldGetTopicMessages_affectWhenRemove() {
        // given
        givenMessages_affectWhenRemove();

        // when then
        // all for topic 1 message in room
        messages.assertThat(3, 4, 11, 14)
                .in(chat.getTopicMessages(ROOM_TOPIC, 1));

        // when then
        // all for topic 2 message in room
        messages.assertThat(8, 10)
                .in(chat.getTopicMessages(ROOM_TOPIC, 2));

        // when then
        // get topic message like room message
        messages.assertThat()
                .in(chat.getTopicMessages(ROOM_TOPIC, 3));

        // when then
        // all for non topic message in room
        messages.assertThat()
                .in(chat.getTopicMessages(ROOM_TOPIC, 4));

        // when
        removeSeveralMessages_affectWhenRemove();

        // when then
        // all for topic 1 message in room
        messages.assertThat(3, 11, 14)
                .in(chat.getTopicMessages(ROOM_TOPIC, 1));

        // when then
        // all for topic 2 message in room
        messages.assertThat(8)
                .in(chat.getTopicMessages(ROOM_TOPIC, 2));

        // when then
        // get topic message like room message
        messages.assertThat()
                .in(chat.getTopicMessages(ROOM_TOPIC, 3));

        // when then
        // all for non topic message in room
        messages.assertThat()
                .in(chat.getTopicMessages(ROOM_TOPIC, 4));
    }

    @Test
    public void shouldGetMessagesBetween_affectWhenRemove() {
        // given
        givenMessages_affectWhenRemove();

        // when then
        // get room messages
        messages.assertThat(1, 2, 7, 13)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(3, 4, 11, 14)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(5, 6, 12)
                .in(chat.getMessagesBetween(FIELD, 1,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(9)
                .in(chat.getMessagesBetween(FIELD, 2,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when
        removeSeveralMessages_affectWhenRemove();

        // when then
        // get room messages
        messages.assertThat(1, 7, 13)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(3, 11, 14)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(5, 12)
                .in(chat.getMessagesBetween(FIELD, 1,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat()
                .in(chat.getMessagesBetween(FIELD, 2,
                        Filter.room("room")
                                .afterId(1)
                                .beforeId(14)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetMessagesAfter_affectWhenRemove() {
        // given
        givenMessages_affectWhenRemove();

        // when then
        // get room messages
        messages.assertThat(1, 2, 7, 13)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(3, 4, 11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(5, 6, 12)
                .in(chat.getMessagesAfter(FIELD, 1,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(9)
                .in(chat.getMessagesAfter(FIELD, 2,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when
        removeSeveralMessages_affectWhenRemove();

        // when then
        // get room messages
        messages.assertThat(1, 7, 13)
                .in(chat.getMessagesAfter(ROOM, null,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(3, 11, 14)
                .in(chat.getMessagesAfter(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(5, 12)
                .in(chat.getMessagesAfter(FIELD, 1,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat()
                .in(chat.getMessagesAfter(FIELD, 2,
                        Filter.room("room")
                                .afterId(1)
                                .inclusive(true)
                                .count(10)
                                .get()));

    }

    @Test
    public void shouldGetMessagesBefore_affectWhenRemove() {
        // given
        givenMessages_affectWhenRemove();

        // when then
        // get room messages
        messages.assertThat(1, 2, 7, 13)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(3, 4, 11, 14)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(5, 6, 12)
                .in(chat.getMessagesBefore(FIELD, 1,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(9)
                .in(chat.getMessagesBefore(FIELD, 2,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));



        // when
        removeSeveralMessages_affectWhenRemove();

        // when then
        // get room messages
        messages.assertThat(1, 7, 13)
                .in(chat.getMessagesBefore(ROOM, null,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get topic messages
        messages.assertThat(3, 11, 14)
                .in(chat.getMessagesBefore(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat(5, 12)
                .in(chat.getMessagesBefore(FIELD, 1,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));

        // when then
        // get field messages
        messages.assertThat()
                .in(chat.getMessagesBefore(FIELD, 2,
                        Filter.room("room")
                                .beforeId(14)
                                .inclusive(true)
                                .count(10)
                                .get()));
    }

    private void removeSeveralMessages_affectWhenRemove() {
        // remove several messages
        // id = 2  // message in room, will be topic2
        chat.deleteMessage("room", 2, "player");

        // id = 4  // message for topic1
        chat.deleteMessage("room", 4, "player");

        // id = 6  // message for field1
        chat.deleteMessage("room", 6, "player");

        // id = 9  // message for field1
        chat.deleteMessage("room", 9, "player");

        // id = 10 // message for topic2
        chat.deleteMessage("room", 10, "player");
    }

    private void givenMessages_affectWhenRemove() {
        messages.post("room", "player");                // id = 1  // message in room, will be topic1
        messages.post("room", "player");                // id = 2  // message in room, will be topic2
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 3  // message for room topic1
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 4  // message for room topic1
        messages.post("room", "player", 1, FIELD);      // id = 5  // message for field1
        messages.post("room", "player", 1, FIELD);      // id = 6  // message for field1
        messages.post("room", "player");                // id = 7  // just another one message in room
        messages.post("room", "player", 2, ROOM_TOPIC); // id = 8  // message for room topic2
        messages.post("room", "player", 2, FIELD);      // id = 9  // message for field1
        messages.post("room", "player", 2, ROOM_TOPIC); // id = 10 // message for room topic2
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 11 // message for room topic1
        messages.post("room", "player", 1, FIELD);      // id = 12 // message for field1
        messages.post("room", "player");                // id = 13 // just another one message in room
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 14 // message for room topic1
    }

    @Test
    public void shouldGetAllMessages_caseTopic() {
        // given
        messages.post("room", "player");                // id = 1 // message in room, will be topic 1
        messages.post("room", "player");                // id = 2 // message in room, will be topic 2
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 3 // message for topic1
        messages.post("room", "player");                // id = 4 // just another one message in room
        messages.post("room", "player", 2, ROOM_TOPIC); // id = 5 // message for topic2
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 6 // message for topic1
        messages.post("room", "player");                // id = 7 // just another one message in room
        messages.post("room", "player", 1, ROOM_TOPIC); // id = 8 // message for topic1

        // when then
        // all for room
        messages.assertThat(1, 2, 4, 7)
                .in(chat.getMessages(ROOM, null,
                        Filter.room("room")
                                .count(10)
                                .get()));

        // when then
        // all for topic 1 message in room
        messages.assertThat(3, 6, 8)
                .in(chat.getMessages(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(MAX)
                                .get()));

        messages.assertThat(6, 8)
                .in(chat.getMessages(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .count(2)
                                .get()));

        // when then
        // all for topic 2 message in room
        messages.assertThat(5)
                .in(chat.getMessages(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(MAX)
                                .get()));

        messages.assertThat(5)
                .in(chat.getMessages(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .count(1)
                                .get()));

        // when then
        // get topic message like room message
        messages.assertThat()
                .in(chat.getMessages(ROOM_TOPIC, 3,
                        Filter.room("room")
                                .count(MAX)
                                .get()));

        // when then
        // all for non topic message in room
        messages.assertThat()
                .in(chat.getMessages(ROOM_TOPIC, 4,
                        Filter.room("room")
                                .count(MAX)
                                .get()));

        // when then
        // between messages in case between 3 ... 8
        messages.assertThat(4, 7)
                .in(chat.getMessagesBetween(ROOM, null,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));

        messages.assertThat(3, 6, 8)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));

        messages.assertThat(6)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 1,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(8)
                                .inclusive(false)
                                .get()));

        messages.assertThat(5)
                .in(chat.getMessagesBetween(ROOM_TOPIC, 2,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));

        messages.assertThat()
                .in(chat.getMessagesBetween(ROOM_TOPIC, 3,
                        Filter.room("room")
                                .afterId(3)
                                .beforeId(8)
                                .inclusive(true)
                                .get()));
    }

    @Test
    public void shouldGetLastFieldId() {
        // when then
        messages.post("room", "player");      // room  chat
        assertEquals(0, chat.getLastFieldId());

        // when then
        messages.post("room", "player");      // room  chat
        assertEquals(0, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 1, FIELD);  // field chat
        assertEquals(1, chat.getLastFieldId());

        // when then
        messages.post("room", "player");      // room  chat
        assertEquals(1, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 1, ROOM_TOPIC);   // topic chat
        assertEquals(1, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 2, ROOM_TOPIC);   // topic chat
        assertEquals(1, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 3, FIELD);  // field chat
        assertEquals(3, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 51, FIELD); // field chat
        assertEquals(51, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 2, FIELD);  // field chat
        assertEquals(51, chat.getLastFieldId());

        // when then
        messages.post("room", "player");      // room  chat
        assertEquals(51, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 60, ROOM_TOPIC);  // topic chat
        assertEquals(51, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 1, ROOM_TOPIC);   // topic chat
        assertEquals(51, chat.getLastFieldId());

        // when then
        assertEquals(51, chat.getLastFieldId());

        // when then
        messages.post("room", "player", 62, FIELD); // field chat
        assertEquals(62, chat.getLastFieldId());
    }

    @Test
    public void shouldGetLastFieldId_doNotAffectWhenRemove() {
        // given
        shouldGetLastFieldId();

        // when then
        messages.removeAll(FIELD, 62, "room",
                "[Chat.Message(id=13, topicId=62, type=FIELD(3), room=room, playerId=player, recipientId=null, time=1615232723345, text=message13)]");

        assertEquals(62, chat.getLastFieldId());
    }

}
