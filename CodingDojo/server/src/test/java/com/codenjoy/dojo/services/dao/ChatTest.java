package com.codenjoy.dojo.services.dao;

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
        Chat.Message message1 = chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:31:15.792+0200"),
                "message1"));

        // then
        assertEquals("Chat.Message(id=0, chatId=room1, playerId=player1, " +
                        "time=1615231875792, text=message1)",
                message1.toString());

        // when
        Chat.Message message2 = chat.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2022-03-08T21:31:15.792+0200"),
                "message2"));

        // then
        assertEquals("Chat.Message(id=1, chatId=room2, playerId=player2, " +
                        "time=1646767875792, text=message2)",
                message2.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedMoreThanPresent() {
        // given
        chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room1", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        // не включен - другая комната
        chat.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when
        List<Chat.Message> messages = chat.getMessages("room1", 10);

        // then
        assertEquals("[Chat.Message(id=0, chatId=room1, playerId=player1, " +
                        "time=1615231423345, text=message1), " +
                        "Chat.Message(id=1, chatId=room1, playerId=player1, " +
                        "time=1615235514756, text=message2), " +
                        "Chat.Message(id=2, chatId=room1, playerId=player2, " +
                        "time=1615240404792, text=message3)]",
                messages.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedLessThanPresent() {
        // given
        chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        // не включен - запросили не так много сообщений
        chat.saveMessage(new Chat.Message("room1", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        // не включен - другая комната
        chat.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when
        List<Chat.Message> messages = chat.getMessages("room1", 2);

        // then
        assertEquals("[Chat.Message(id=0, chatId=room1, playerId=player1, " +
                        "time=1615231423345, text=message1), " +
                        "Chat.Message(id=1, chatId=room1, playerId=player1, " +
                        "time=1615235514756, text=message2)]",
                messages.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenNoSuchRoom() {
        // given
        chat.saveMessage(new Chat.Message("room1", "player1",
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
        chat.saveMessage(new Chat.Message("room1", "player1",
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
        chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room1", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        chat.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when then
        assertEquals("Chat.Message(id=0, chatId=room1, playerId=player1, " +
                        "time=1615231423345, text=message1)",
                chat.getMessageById(0).toString());

        assertEquals("Chat.Message(id=1, chatId=room1, playerId=player1, " +
                        "time=1615235514756, text=message2)",
                chat.getMessageById(1).toString());

        assertEquals("Chat.Message(id=2, chatId=room1, playerId=player2, " +
                        "time=1615240404792, text=message3)",
                chat.getMessageById(2).toString());

        assertEquals("Chat.Message(id=3, chatId=room2, playerId=player2, " +
                        "time=1615240404792, text=message4)",
                chat.getMessageById(3).toString());
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
        chat.saveMessage(new Chat.Message("room", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        chat.saveMessage(new Chat.Message("room", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        chat.saveMessage(new Chat.Message("room", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        chat.saveMessage(new Chat.Message("room", "player3",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.793+0200"),
                "message4"));

        assertEquals("[Chat.Message(id=0, chatId=room, playerId=player1, " +
                        "time=1615231423345, text=message1), " +
                        "Chat.Message(id=1, chatId=room, playerId=player1, " +
                        "time=1615235514756, text=message2), " +
                        "Chat.Message(id=2, chatId=room, playerId=player2, " +
                        "time=1615240404792, text=message3), " +
                        "Chat.Message(id=3, chatId=room, playerId=player3, " +
                        "time=1615240404793, text=message4)]",
                chat.getMessages("room", 10).toString());

        // when
        chat.deleteMessage(0);

        // then
        assertEquals("[Chat.Message(id=1, chatId=room, playerId=player1, " +
                        "time=1615235514756, text=message2), " +
                        "Chat.Message(id=2, chatId=room, playerId=player2, " +
                        "time=1615240404792, text=message3), " +
                        "Chat.Message(id=3, chatId=room, playerId=player3, " +
                        "time=1615240404793, text=message4)]",
                chat.getMessages("room", 10).toString());

        // when
        chat.deleteMessage(2);

        // then
        assertEquals("[Chat.Message(id=1, chatId=room, playerId=player1, " +
                        "time=1615235514756, text=message2), " +
                        "Chat.Message(id=3, chatId=room, playerId=player3, " +
                        "time=1615240404793, text=message4)]",
                chat.getMessages("room", 10).toString());

        // when
        chat.deleteMessage(3);

        // then
        assertEquals("[Chat.Message(id=1, chatId=room, playerId=player1, " +
                        "time=1615235514756, text=message2)]",
                chat.getMessages("room", 10).toString());
    }

    @Test
    public void shouldDeleteMessageById_whenNotExists() {
        // when then
        chat.deleteMessage(100500);
    }
}
