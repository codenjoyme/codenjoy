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
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class ChatTest {

    public static final int MAX = 100;
    private Chat chat;

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
    }

    @After
    public void tearDown() {
        chat.removeDatabase();
    }

    @Test
    public void shouldGenerateId_whenSaveMessage() {
        // when
        Chat.Message message1 = chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:31:15.792+0200"),
                "message1"));

        // then
        assertEquals("Chat.Message(id=0, topicId=null, room=room1, playerId=player1, time=1615231875792, text=message1)",
                message1.toString());

        // when
        Chat.Message message2 = chat.saveMessage(new Chat.Message("room2", null, "player2",
                JDBCTimeUtils.getTimeLong("2022-03-08T21:31:15.792+0200"),
                "message2"));

        // then
        assertEquals("Chat.Message(id=1, topicId=null, room=room2, playerId=player2, time=1646767875792, text=message2)",
                message2.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedMoreThanPresent() {
        // given
        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room1", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        // не включен - другая комната
        chat.saveMessage(new Chat.Message("room2", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when
        List<Chat.Message> messages = chat.getMessages("room1", 10);

        // then
        assertEquals("[Chat.Message(id=1, topicId=null, room=room1, playerId=player1, time=1615231423345, text=message1), " +
                        "Chat.Message(id=2, topicId=null, room=room1, playerId=player1, time=1615235514756, text=message2), " +
                        "Chat.Message(id=3, topicId=null, room=room1, playerId=player2, time=1615240404792, text=message3)]",
                messages.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedLessThanPresent() {
        // given
        // так как getMessages берет только последние сообщения
        // это сообщение не включено - запросили не так много сообщений
        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room1", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        // не включен - другая комната
        chat.saveMessage(new Chat.Message("room2", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when
        List<Chat.Message> messages = chat.getMessages("room1", 2);

        // then
        assertEquals("[Chat.Message(id=2, topicId=null, room=room1, playerId=player1, time=1615235514756, text=message2), " +
                        "Chat.Message(id=3, topicId=null, room=room1, playerId=player2, time=1615240404792, text=message3)]",
                messages.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenNoSuchRoom() {
        // given
        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        // when
        List<Chat.Message> messages = chat.getMessages("room2", 2);

        // then
        assertEquals("[]",
                messages.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenZeroMessages() {
        // given
        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        // when
        List<Chat.Message> messages = chat.getMessages("room1", 0);

        // then
        assertEquals("[]",
                messages.toString());
    }

    @Test
    public void shouldGetMessageById() {
        // given
        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room1", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room1", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        chat.saveMessage(new Chat.Message("room2", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when then
        assertEquals("Chat.Message(id=1, topicId=null, room=room1, playerId=player1, time=1615231423345, text=message1)",
                chat.getMessageById(1).toString());

        assertEquals("Chat.Message(id=2, topicId=null, room=room1, playerId=player1, time=1615235514756, text=message2)",
                chat.getMessageById(2).toString());

        assertEquals("Chat.Message(id=3, topicId=null, room=room1, playerId=player2, time=1615240404792, text=message3)",
                chat.getMessageById(3).toString());

        assertEquals("Chat.Message(id=4, topicId=null, room=room2, playerId=player2, time=1615240404792, text=message4)",
                chat.getMessageById(4).toString());
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
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        chat.saveMessage(new Chat.Message("room", null, "player3",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.793+0200"),
                "message4"));

        assertEquals("[Chat.Message(id=1, topicId=null, room=room, playerId=player1, time=1615231423345, text=message1), " +
                        "Chat.Message(id=2, topicId=null, room=room, playerId=player1, time=1615235514756, text=message2), " +
                        "Chat.Message(id=3, topicId=null, room=room, playerId=player2, time=1615240404792, text=message3), " +
                        "Chat.Message(id=4, topicId=null, room=room, playerId=player3, time=1615240404793, text=message4)]",
                chat.getMessages("room", 10).toString());

        // when
        chat.deleteMessage(1);

        // then
        assertEquals("[Chat.Message(id=2, topicId=null, room=room, playerId=player1, time=1615235514756, text=message2), " +
                        "Chat.Message(id=3, topicId=null, room=room, playerId=player2, time=1615240404792, text=message3), " +
                        "Chat.Message(id=4, topicId=null, room=room, playerId=player3, time=1615240404793, text=message4)]",
                chat.getMessages("room", 10).toString());

        // when
        chat.deleteMessage(3);

        // then
        assertEquals("[Chat.Message(id=2, topicId=null, room=room, playerId=player1, time=1615235514756, text=message2), " +
                        "Chat.Message(id=4, topicId=null, room=room, playerId=player3, time=1615240404793, text=message4)]",
                chat.getMessages("room", 10).toString());

        // when
        chat.deleteMessage(4);

        // then
        assertEquals("[Chat.Message(id=2, topicId=null, room=room, playerId=player1, time=1615235514756, text=message2)]",
                chat.getMessages("room", 10).toString());
    }

    @Test
    public void shouldDeleteMessageById_whenNotExists() {
        // when then
        chat.deleteMessage(100500);
    }

    @Test
    public void shouldGetMessagesAfterId() {
        // given
        // id = 0
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        // id = 1
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message2"));

        // id = 2
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message3"));

        // id = 3
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message4"));

        // id = 4
        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message5"));

        // id = 5
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message6"));

        // id = 6
        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message7"));

        // id = 7
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message8"));

        // when then
        // первое сообщение c id = afterId не включается
        assertEquals("[Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player2, time=1615240404792, text=message7)]",
                chat.getMessagesAfter("room", MAX, 1).toString());

        // берутся только два сверху, хотя доступные 3
        assertEquals("[Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5)]",
                chat.getMessagesAfter("room", 2, 1).toString());

        // можно указывать даже айдишку из другого чата - они порядковые
        assertEquals("[Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player2, time=1615240404792, text=message7)]",
                chat.getMessagesAfter("room", MAX, 2).toString());

        // минус одно сообщение с id = afterId
        assertEquals("[Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player2, time=1615240404792, text=message7)]",
                chat.getMessagesAfter("room", MAX, 4).toString());

        // минус одно сообщение с id = afterId
        assertEquals("[]",
                chat.getMessagesAfter("room", MAX, 7).toString());
    }

    @Test
    public void shouldGetMessagesBeforeId() {
        // given
        // id = 0
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        // id = 1
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message2"));

        // id = 2
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message3"));

        // id = 3
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message4"));

        // id = 4
        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message5"));

        // id = 5
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message6"));

        // id = 6
        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message7"));

        // id = 7
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message8"));

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        assertEquals("[Chat.Message(id=1, topicId=null, room=room, playerId=player1, time=1615231423345, text=message1), " +
                        "Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player2, time=1615240404792, text=message7)]",
                chat.getMessagesBefore("room", MAX, 8).toString());

        // первое сообщение c id = beforeId не включается
        assertEquals("[Chat.Message(id=1, topicId=null, room=room, playerId=player1, time=1615231423345, text=message1), " +
                        "Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5)]",
                chat.getMessagesBefore("room", MAX, 7).toString());

        // берутся только два но с конца, хотя доступные 3
        assertEquals("[Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5)]",
                chat.getMessagesBefore("room", 2, 7).toString());

        // минус одно сообщение с id = beforeId
        assertEquals("[Chat.Message(id=1, topicId=null, room=room, playerId=player1, time=1615231423345, text=message1), " +
                        "Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3)]",
                chat.getMessagesBefore("room", MAX, 5).toString());

        // минус одно сообщение с id = beforeId
        assertEquals("[]",
                chat.getMessagesBefore("room", MAX, 1).toString());
    }

    @Test
    public void shouldGetMessagesBetweenIds() {
        // given
        // id = 0
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        // id = 1
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message2"));

        // id = 2
        chat.saveMessage(new Chat.Message("room", null, "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message3"));

        // id = 3
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message4"));

        // id = 4
        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message5"));

        // id = 5
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message6"));

        // id = 6
        chat.saveMessage(new Chat.Message("room", null, "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message7"));

        // id = 7
        // другой чат - не берем
        chat.saveMessage(new Chat.Message("otherRoom", null, "otherPlayer",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message8"));

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        // но первое сообщение c id = afterId не включается
        assertEquals("[Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player2, time=1615240404792, text=message7)]",
                chat.getMessagesBetween("room", 1, 8).toString());

        // первое сообщение c id = beforeId не включается
        // так же как и последнее сообщение c id = afterId тоже не включается
        assertEquals("[Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615235514756, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5)]",
                chat.getMessagesBetween("room", 1, 7).toString());

        // минус одно сообщение с id = beforeId
        // минус одно сообщение с id = afterId
        assertEquals("[Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615240404792, text=message5)]",
                chat.getMessagesBetween("room", 3, 7).toString());

        assertEquals("[]",
                chat.getMessagesBetween("room", 5, 5).toString());
    }

    @Test
    public void shouldGetAllTopicMessages() {
        // given
        // message in room, will be topic 1
        // id = 1
        chat.saveMessage(new Chat.Message("room", null, "player", 12345L, "message1"));

        // message in room, will be topic 2
        // id = 2
        chat.saveMessage(new Chat.Message("room", null, "player", 12346L, "message2"));

        // message for topic1
        // id = 3
        chat.saveMessage(new Chat.Message("room", 1, "player", 123467L, "message3"));

        // just another one message in room
        // id = 4
        chat.saveMessage(new Chat.Message("room", null, "player", 123468L, "message4"));

        // message for topic2
        // id = 5
        chat.saveMessage(new Chat.Message("room", 2, "player", 123469L, "message5"));

        // message for topic1
        // id = 6
        chat.saveMessage(new Chat.Message("room", 1, "player", 123470L, "message6"));

        // just another one message in room
        // id = 7
        chat.saveMessage(new Chat.Message("room", null, "player", 123471L, "message7"));

        // message for topic1
        // id = 8
        chat.saveMessage(new Chat.Message("room", 1, "player", 123472L, "message8"));

        // when then
        // all for room
        assertEquals("[Chat.Message(id=1, topicId=null, room=room, playerId=player, time=12345, text=message1), " +
                        "Chat.Message(id=2, topicId=null, room=room, playerId=player, time=12346, text=message2), " +
                        "Chat.Message(id=4, topicId=null, room=room, playerId=player, time=123468, text=message4), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player, time=123471, text=message7)]",
                chat.getMessages("room", 10).toString());

        // when then
        // all for topic 1 message in room
        assertEquals("[Chat.Message(id=3, topicId=1, room=room, playerId=player, time=123467, text=message3), " +
                        "Chat.Message(id=6, topicId=1, room=room, playerId=player, time=123470, text=message6), " +
                        "Chat.Message(id=8, topicId=1, room=room, playerId=player, time=123472, text=message8)]",
                chat.getTopicMessages(1).toString());

        // when then
        // all for topic 2 message in room
        assertEquals("[Chat.Message(id=5, topicId=2, room=room, playerId=player, time=123469, text=message5)]",
                chat.getTopicMessages(2).toString());

        // when then
        // get topic message like room message
        assertEquals("[]",
                chat.getTopicMessages(3).toString());

        // when then
        // all for non topic message in room
        assertEquals("[]",
                chat.getTopicMessages(4).toString());

        // when then
        // between messages in topic -> room messages between 3 ... 8
        assertEquals("[Chat.Message(id=4, topicId=null, room=room, playerId=player, time=123468, text=message4), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player, time=123471, text=message7)]",
                chat.getMessagesBetween("room", 3, 8).toString());
    }
}
