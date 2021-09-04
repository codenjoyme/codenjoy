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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class ChatTest {

    public static final int MAX = 100;
    private Chat chat;

    private List<Chat.Message> messages = new LinkedList<>();

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
        Chat.Message message1 = addMessage("room1", "player1");

        // then
        assertEquals("Chat.Message(id=1, topicId=null, room=room1, playerId=player1, time=1615231523345, text=message1)",
                message1.toString());

        // when
        Chat.Message message2 = addMessage("room2", "player2");

        // then
        assertEquals("Chat.Message(id=2, topicId=null, room=room2, playerId=player2, time=1615231623345, text=message2)",
                message2.toString());
    }

    @Test
    public void shouldGetLastMessageId_whenAddedTopicMessages() {
        // when then
        assertEquals(null, chat.getLastMessageId("room1"));

        // when then
        addMessage("room1", "player1"); // id = 1
        assertEquals(Integer.valueOf(1), chat.getLastMessageId("room1"));

        // when then
        addMessage("room1", "player1", 1); // id = 2
        assertEquals(Integer.valueOf(1), chat.getLastMessageId("room1"));

        // when then
        addMessage("room1", "player1", 1); // id = 3
        assertEquals(Integer.valueOf(1), chat.getLastMessageId("room1"));
    }

    @Test
    public void shouldGetLastMessageIds_whenAddedTopicMessages() {
        // when then
        assertEquals("{}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player1"); // id = 1
        assertEquals("{room1=1}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player1", 1); // id = 2
        assertEquals("{room1=1}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player1", 1); // id = 3
        assertEquals("{room1=1}",
                chat.getLastMessageIds().toString());
    }

    @Test
    public void shouldGetLastMessageId() {
        // when then
        assertEquals(null, chat.getLastMessageId("room1"));
        assertEquals("{}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player1"); // id = 1
        assertEquals(Integer.valueOf(1), chat.getLastMessageId("room1"));
        assertEquals("{room1=1}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player1"); // id = 2
        assertEquals(Integer.valueOf(2), chat.getLastMessageId("room1"));
        assertEquals("{room1=2}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player2"); // id = 3
        assertEquals(Integer.valueOf(3), chat.getLastMessageId("room1"));
        assertEquals(null, chat.getLastMessageId("room2"));
        assertEquals("{room1=3}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room2", "player2"); // id = 4 // не включен - другая комната
        assertEquals(Integer.valueOf(3), chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4), chat.getLastMessageId("room2"));
        assertEquals("{room1=3, room2=4}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room1", "player2"); // id = 5
        assertEquals(Integer.valueOf(5), chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4), chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{room2=4, room1=5}",
                chat.getLastMessageIds().toString());

        // when then
        addMessage("room3", "player1"); // id = 6
        assertEquals(Integer.valueOf(5), chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4), chat.getLastMessageId("room2"));
        assertEquals(Integer.valueOf(6), chat.getLastMessageId("room3"));
        assertEquals("{room2=4, room1=5, room3=6}",
                chat.getLastMessageIds().toString());

        // when then
        chat.deleteMessage("room3", 6, "player1");
        assertEquals(Integer.valueOf(5), chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4), chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{room2=4, room1=5}",
                chat.getLastMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 5, "player2");
        assertEquals(Integer.valueOf(3), chat.getLastMessageId("room1"));
        assertEquals(Integer.valueOf(4), chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{room1=3, room2=4}",
                chat.getLastMessageIds().toString());

        // when then
        chat.deleteMessage("room2", 4, "player2");
        assertEquals(Integer.valueOf(3), chat.getLastMessageId("room1"));
        assertEquals(null, chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{room1=3}",
                chat.getLastMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 3, "player2");
        assertEquals(Integer.valueOf(2), chat.getLastMessageId("room1"));
        assertEquals(null, chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{room1=2}",
                chat.getLastMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 2, "player1");
        assertEquals(Integer.valueOf(1), chat.getLastMessageId("room1"));
        assertEquals(null, chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{room1=1}",
                chat.getLastMessageIds().toString());

        // when then
        chat.deleteMessage("room1", 1, "player1");
        assertEquals(null, chat.getLastMessageId("room1"));
        assertEquals(null, chat.getLastMessageId("room2"));
        assertEquals(null, chat.getLastMessageId("room3"));
        assertEquals("{}",
                chat.getLastMessageIds().toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedMoreThanPresent() {
        // given
        addMessage("room1", "player1"); // id = 1
        addMessage("room1", "player1"); // id = 2
        addMessage("room1", "player2"); // id = 3
        addMessage("room2", "player2"); // id = 4 // не включен - другая комната

        // when then
        assertThat(1, 2, 3)
                .in(chat.getMessages("room1", 10));
    }

    public class AssertThat {

        private List<Integer> expectedIds;

        public AssertThat(int[] ids) {
            expectedIds = Arrays.stream(ids).mapToObj(i -> i).collect(toList());
        }

        public void in(List<Chat.Message> actual) {
            List<Integer> actualIds = actual.stream()
                    .map(message -> message.getId())
                    .collect(toList());

            assertEquals(expectedIds.toString(), actualIds.toString());

            List<Chat.Message> expected = ChatTest.this.messages.stream()
                    .filter(message -> isPresent(actual, message))
                    .collect(toList());

            assertEquals(ChatTest.toString(expected),
                    ChatTest.toString(actual));
        }

        public boolean isPresent(List<Chat.Message> list, Chat.Message found) {
            return list.stream()
                    .filter(message -> found.getId() == message.getId())
                    .findFirst()
                    .isPresent();
        }
    }

    public static String toString(List<Chat.Message> messages) {
        return messages.toString()
                .replace("), Chat.Message(",
                        "), \nChat.Message(");
    }

    private AssertThat assertThat(int... ids) {
        return new AssertThat(ids);
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedLessThanPresent() {
        // given
        // так как getMessages берет только последние сообщения
        // это сообщение не включено - запросили не так много сообщений
        addMessage("room1", "player1");
        addMessage("room1", "player1");
        addMessage("room1", "player2");
        addMessage("room2", "player2"); // не включен - другая комната

        // when then
        assertThat(2, 3)
                .in(chat.getMessages("room1", 2));
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenNoSuchRoom() {
        // given
        addMessage("room1", "player1");
        
        // when then
        assertThat()
                .in(chat.getMessages("room2", 2));
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenZeroMessages() {
        // given
        addMessage("room1", "player1");

        // when then
        assertThat()
                .in(chat.getMessages("room1", 0));
    }

    @Test
    public void shouldGetMessageById() {
        // given
        addMessage("room1", "player1");
        addMessage("room1", "player1");
        addMessage("room1", "player2");
        addMessage("room2", "player2");

        // when then
        assertEquals("Chat.Message(id=1, topicId=null, room=room1, playerId=player1, time=1615231523345, text=message1)",
                chat.getMessageById(1).toString());

        assertEquals("Chat.Message(id=2, topicId=null, room=room1, playerId=player1, time=1615231623345, text=message2)",
                chat.getMessageById(2).toString());

        assertEquals("Chat.Message(id=3, topicId=null, room=room1, playerId=player2, time=1615231723345, text=message3)",
                chat.getMessageById(3).toString());

        assertEquals("Chat.Message(id=4, topicId=null, room=room2, playerId=player2, time=1615231823345, text=message4)",
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
        addMessage("room", "player1"); // id = 1
        addMessage("room", "player1"); // id = 2
        addMessage("room", "player2"); // id = 3
        addMessage("room", "player3"); // id = 4

        // when then
        assertThat(1, 2, 3, 4)
                .in(chat.getMessages("room", 10));

        // when
        assertEquals(true, chat.deleteMessage("room", 1, "player1"));

        // then
        assertThat(2, 3, 4)
                .in(chat.getMessages("room", 10));

        // when
        assertEquals(true, chat.deleteMessage("room", 3, "player2"));

        // then
        assertThat(2, 4)
                .in(chat.getMessages("room", 10));

        // when
        assertEquals(true, chat.deleteMessage("room", 4, "player3"));

        // then
        assertThat(2)
                .in(chat.getMessages("room", 10));
    }

    @Test
    public void shouldDeleteMessageById_whenRoomNotExists() {
        // given
        addMessage("room", "player"); // id = 1

        // when then
        assertEquals(false, chat.deleteMessage("otherRoom", 1, "player"));
    }

    @Test
    public void shouldDeleteMessageById_whenInvalidRoom() {
        // given
        addMessage("room", "player"); // id = 1

        // when then
        assertEquals(false, chat.deleteMessage("room", 100500, "player"));
    }

    @Test
    public void shouldDeleteMessageById_whenOtherPlayer() {
        // given
        addMessage("room", "player");

        // then
        assertThat(1)
                .in(chat.getMessages("room", 10));

        // when then
        assertEquals(false, chat.deleteMessage("room", 1, "otherPlayer"));
    }

    @Test
    public void shouldGetMessagesAfterId() {
        // given
        givenCase();

        // when then
        // первое сообщение c id = afterId не включается
        assertThat(3, 5, 7)
                .in(chat.getMessagesAfter("room", MAX, 1, false));

        // то же только c inclusive = true
        assertThat(1, 3, 5, 7)
                .in(chat.getMessagesAfter("room", MAX, 1, true));

        // берутся только два сверху, хотя доступные 3
        assertThat(3, 5)
                .in(chat.getMessagesAfter("room", 2, 1, false));

        // то же только c inclusive = true
        assertThat(1, 3)
                .in(chat.getMessagesAfter("room", 2, 1, true));

        // можно указывать даже айдишку из другого чата - они порядковые
        assertThat(3, 5, 7)
                .in(chat.getMessagesAfter("room", MAX, 2, false));

        // то же только c inclusive = true
        assertThat(3, 5, 7)
                .in(chat.getMessagesAfter("room", MAX, 2, true));

        // минус одно сообщение с id = afterId
        assertThat(5, 7)
                .in(chat.getMessagesAfter("room", MAX, 4, false));

        // то же только c inclusive = true
        assertThat(5, 7)
                .in(chat.getMessagesAfter("room", MAX, 4, true));

        // минус одно сообщение с id = afterId
        assertThat()
                .in(chat.getMessagesAfter("room", MAX, 7, false));

        // то же только c inclusive = true
        assertThat(7)
                .in(chat.getMessagesAfter("room", MAX, 7, true));
    }

    @Test
    public void shouldGetMessagesBeforeId() {
        // given
        givenCase();

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBefore("room", MAX, 8, false));

        // то же только c inclusive = true
        assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBefore("room", MAX, 8, true));

        // первое сообщение c id = beforeId не включается
        assertThat(1, 3, 5)
                .in(chat.getMessagesBefore("room", MAX, 7, false));

        // то же только c inclusive = true
        assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBefore("room", MAX, 7, true));

        // берутся только два но с конца, хотя доступные 3
        assertThat(3, 5)
                .in(chat.getMessagesBefore("room", 2, 7, false));

        // то же только c inclusive = true
        assertThat(5, 7)
                .in(chat.getMessagesBefore("room", 2, 7, true));

        // минус одно сообщение с id = beforeId
        assertThat(1, 3)
                .in(chat.getMessagesBefore("room", MAX, 5, false));

        // то же только c inclusive = true
        assertThat(1, 3, 5)
                .in(chat.getMessagesBefore("room", MAX, 5, true));

        // минус одно сообщение с id = beforeId
        assertThat()
                .in(chat.getMessagesBefore("room", MAX, 1, false));

        // то же только c inclusive = true
        assertThat(1)
                .in(chat.getMessagesBefore("room", MAX, 1, true));
    }

    @Test
    public void shouldGetMessagesBetweenIds() {
        // given
        givenCase();

        // when then
        // можно указывать даже айдишку из другого чата - они порядковые
        // но первое сообщение c id = afterId не включается
        assertThat(3, 5, 7)
                .in(chat.getMessagesBetween("room", 1, 8, false));

        // то же только c inclusive = true
        assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBetween("room", 1, 8, true));

        // первое сообщение c id = beforeId не включается
        // так же как и последнее сообщение c id = afterId тоже не включается
        assertThat(3, 5)
                .in(chat.getMessagesBetween("room", 1, 7, false));

        // то же только c inclusive = true
        assertThat(1, 3, 5, 7)
                .in(chat.getMessagesBetween("room", 1, 7, true));

        // минус одно сообщение с id = beforeId
        // минус одно сообщение с id = afterId
        assertThat(5)
                .in(chat.getMessagesBetween("room", 3, 7, false));

        // то же только c inclusive = true
        assertThat(3, 5, 7)
                .in(chat.getMessagesBetween("room", 3, 7, true));

        // когда встретились на границе
        assertThat()
                .in(chat.getMessagesBetween("room", 5, 5, false));

        // то же только c inclusive = true
        assertThat(5)
                .in(chat.getMessagesBetween("room", 5, 5, true));
    }

    public void givenCase() {
        addMessage("room", "player1");          // id = 1
        addMessage("otherRoom", "otherPlayer"); // id = 2 // другой чат - не берем
        addMessage("room", "player1");          // id = 3
        addMessage("otherRoom", "otherPlayer"); // id = 4 // другой чат - не берем
        addMessage("room", "player2");          // id = 5
        addMessage("otherRoom", "otherPlayer"); // id = 6 // другой чат - не берем
        addMessage("room", "player2");          // id = 7
        addMessage("otherRoom", "otherPlayer"); // id = 8 // другой чат - не берем

        assertEquals("[Chat.Message(id=1, topicId=null, room=room, playerId=player1, time=1615231523345, text=message1), " +
                        "Chat.Message(id=3, topicId=null, room=room, playerId=player1, time=1615231723345, text=message3), " +
                        "Chat.Message(id=5, topicId=null, room=room, playerId=player2, time=1615231923345, text=message5), " +
                        "Chat.Message(id=7, topicId=null, room=room, playerId=player2, time=1615232123345, text=message7)]",
                chat.getMessages("room", MAX).toString());

        assertEquals("[Chat.Message(id=2, topicId=null, room=otherRoom, playerId=otherPlayer, time=1615231623345, text=message2), " +
                        "Chat.Message(id=4, topicId=null, room=otherRoom, playerId=otherPlayer, time=1615231823345, text=message4), " +
                        "Chat.Message(id=6, topicId=null, room=otherRoom, playerId=otherPlayer, time=1615232023345, text=message6), " +
                        "Chat.Message(id=8, topicId=null, room=otherRoom, playerId=otherPlayer, time=1615232223345, text=message8)]",
                chat.getMessages("otherRoom", MAX).toString());
    }

    public Chat.Message addMessage(String room, String player) {
        return addMessage(room, player, null);
    }

    public Chat.Message addMessage(String room, String player, Integer topicId) {
        long time = JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200");
        int index = messages.size() + 1;
        Chat.Message message = new Chat.Message(
                room, topicId, player,
                time + 100000L * index,
                "message" + index);
        Chat.Message added = chat.saveMessage(message);
        messages.add(added);
        return added;
    }

    @Test
    public void shouldGetAllTopicMessages() {
        // given
        addMessage("room", "player");       // id = 1 // message in room, will be topic 1
        addMessage("room", "player");       // id = 2 // message in room, will be topic 2
        addMessage("room", "player", 1);    // id = 3 // message for topic1
        addMessage("room", "player");       // id = 4 // just another one message in room
        addMessage("room", "player", 2);    // id = 5 // message for topic2
        addMessage("room", "player", 1);    // id = 6 // message for topic1
        addMessage("room", "player");       // id = 7 // just another one message in room
        addMessage("room", "player", 1);    // id = 8 // message for topic1

        // when then
        // all for room
        assertThat(1, 2, 4, 7)
                .in(chat.getMessages("room", 10));

        // when then
        // all for topic 1 message in room
        assertThat(3, 6, 8)
                .in(chat.getTopicMessages(1));

        // when then
        // all for topic 2 message in room
        assertThat(5)
                .in(chat.getTopicMessages(2));

        // when then
        // get topic message like room message
        assertThat()
                .in(chat.getTopicMessages(3));

        // when then
        // all for non topic message in room
        assertThat()
                .in(chat.getTopicMessages(4));

        // when then
        // between messages in topic -> room messages between 3 ... 8
        assertThat(4, 7)
                .in(chat.getMessagesBetween("room", 3, 8, false));
    }
}
