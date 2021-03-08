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

    private Chat service;

    @Before
    public void setup() {
        String dbFile = "target/chat.db" + new Random().nextInt();
        service = new Chat(
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
        service.removeDatabase();
    }

    @Test
    public void shouldGenerateId_whenSaveMessage() {
        // when
        Chat.Message message1 = service.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:31:15.792+0200"),
                "message1"));

        // then
        assertEquals("Chat.Message(id=0, roomId=room1, playerId=player1, " +
                        "time=1615231875792, text=message1)",
                message1.toString());

        // when
        Chat.Message message2 = service.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2022-03-08T21:31:15.792+0200"),
                "message2"));

        // then
        assertEquals("Chat.Message(id=1, roomId=room2, playerId=player2, " +
                        "time=1646767875792, text=message2)",
                message2.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedMoreThanPresent() {
        // given
        service.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        service.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        service.saveMessage(new Chat.Message("room1", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        // не включен - другая комната
        service.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when
        List<Chat.Message> messages = service.getMessages("room1", 10);

        // then
        assertEquals("[Chat.Message(id=0, roomId=room1, playerId=player1, " +
                        "time=1615231423345, text=message1), " +
                        "Chat.Message(id=1, roomId=room1, playerId=player1, " +
                        "time=1615235514756, text=message2), " +
                        "Chat.Message(id=2, roomId=room1, playerId=player2, " +
                        "time=1615240404792, text=message3)]",
                messages.toString());
    }

    @Test
    public void shouldGetAllMessages_onlyForThisRoom_whenRequestedLessThanPresent() {
        // given
        service.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200"),
                "message1"));

        service.saveMessage(new Chat.Message("room1", "player1",
                JDBCTimeUtils.getTimeLong("2021-03-08T22:31:54.756+0200"),
                "message2"));

        // не включен - запросили не так много сообщений
        service.saveMessage(new Chat.Message("room1", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message3"));

        // не включен - другая комната
        service.saveMessage(new Chat.Message("room2", "player2",
                JDBCTimeUtils.getTimeLong("2021-03-08T23:53:24.792+0200"),
                "message4"));

        // when
        List<Chat.Message> messages = service.getMessages("room1", 2);

        // then
        assertEquals("[Chat.Message(id=0, roomId=room1, playerId=player1, " +
                        "time=1615231423345, text=message1), " +
                        "Chat.Message(id=1, roomId=room1, playerId=player1, " +
                        "time=1615235514756, text=message2)]",
                messages.toString());
    }
}
