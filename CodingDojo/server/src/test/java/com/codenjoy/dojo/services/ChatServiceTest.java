package com.codenjoy.dojo.services;

import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.ChatTest;
import com.codenjoy.dojo.services.multiplayer.GameField;
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

import static org.junit.Assert.assertEquals;
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

    @SpyBean
    private FieldService fields;

    private List<Chat.Message> messages = new LinkedList<>();

    public Chat.Message addMessage(String room, String player, Integer topicId) {
        return ChatTest.addMessage(chat, messages, room, player, topicId);
    }

    @Before
    public void setup() {
        chat.removeAll();
        fields.removeAll();
    }

    @Test
    public void shouldGetLastMessage() {
        // given
        // random values, don't look for systems here
        addMessage("room1", "player1", null); // 1
        addMessage("room2", "player2", null); // 2
        addMessage("room1", "player3", null); // 3  last room1
        addMessage("room2", "player2", null); // 4  last room2
        addMessage("room1", "player1", 1);    // 5
        addMessage("room2", "player2", 2);    // 6
        addMessage("room1", "player1", 1);    // 7  last topic1
        addMessage("room2", "player2", 2);    // 8
        addMessage("room1", "player2", 4);    // 9
        addMessage("room2", "player3", 3);    // 10 last topic3
        addMessage("room2", "player2", 2);    // 11 last topic2
        addMessage("room1", "player2", 4);    // 12 last topic4
        addMessage("room1", "player1", -1);   // 13
        addMessage("room1", "player1", -1);   // 14
        addMessage("room2", "player3", -3);   // 15
        addMessage("room1", "player1", -1);   // 16 last field1
        addMessage("room1", "player2", -4);   // 17
        addMessage("room2", "player2", -2);   // 18 last field2
        addMessage("room2", "player3", -3);   // 19 last field3
        addMessage("room1", "player2", -4);   // 20 last field4

        // when then
        ChatService.LastMessage last = service.getLast();
        assertEquals("LastMessage{room={room1=3, room2=4}, " +
                        "topic={1=7, 3=10, 2=11, 4=12, -1=16, -2=18, -3=19, -4=20}}",
                last.toString());

        // when then
        assertEquals(3, (int) last.inRoom(dealFor("room1")));
        assertEquals(4, (int) last.inRoom(dealFor("room2")));

        assertEquals(16, (int) last.inField(dealForField(1)));
        assertEquals(18, (int) last.inField(dealForField(2)));
        assertEquals(19, (int) last.inField(dealForField(3)));
        assertEquals(20, (int) last.inField(dealForField(4)));
    }

    private Deal dealForField(int fieldId) {
        GameField field = mock(GameField.class);

        Game game = mock(Game.class);
        when(game.getField()).thenReturn(field);

        fields.register(field);
        when(fields.id(field)).thenReturn(fieldId);

        return new Deal(new Player(), game, null);
    }

    private Deal dealFor(String room) {
        return new Deal(new Player(), null, room);
    }
}