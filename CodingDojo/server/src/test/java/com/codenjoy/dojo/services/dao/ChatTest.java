package com.codenjoy.dojo.services.dao;

import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}
