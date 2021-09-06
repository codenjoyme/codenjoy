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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.TestUtils.assertException;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
public class FieldServiceTest {

    @Autowired
    private FieldService fields;

    @Autowired
    private Chat chat;

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
    public void shouldLoadLastIdFromChat() {
        // given
        // random values, don't look for systems here
        // room chat
        addMessage("room1", "player1", null); //
        addMessage("room2", "player2", null); //
        addMessage("room1", "player3", null); //
        addMessage("room2", "player2", null); //
        // topic chat
        addMessage("room1", "player1", 1);    //
        addMessage("room2", "player2", 2);    //
        addMessage("room1", "player1", 1);    //
        addMessage("room2", "player2", 2);    //
        addMessage("room2", "player2", 2);    //
        // field chat
        addMessage("room1", "player1", -1);   //
        addMessage("room1", "player1", -5);   // max fieldId
        addMessage("room1", "player2", -4);   //
        addMessage("room2", "player3", -3);   //

        // when
        fields.init();

        // and when
        GameField field = mock(GameField.class);
        fields.register(field);

        // then
        assertEquals(6, fields.id(field));

        // when
        GameField field2 = mock(GameField.class);
        fields.register(field2);

        // then
        assertEquals(6, fields.id(field));
        assertEquals(7, fields.id(field2));
    }

    @Test
    public void shouldNextId_increment() {
        // when
        GameField field1 = mock(GameField.class);
        fields.register(field1);

        // then
        assertEquals(1, fields.id(field1));

        // when
        GameField field2 = mock(GameField.class);
        fields.register(field2);

        // then
        assertEquals(1, fields.id(field1));
        assertEquals(2, fields.id(field2));

        // when
        GameField field3 = mock(GameField.class);
        fields.register(field3);

        // then
        assertEquals(1, fields.id(field1));
        assertEquals(2, fields.id(field2));
        assertEquals(3, fields.id(field3));
    }

    @Test
    public void shouldRemove() {
        // given
        GameField field1 = mock(GameField.class);
        fields.register(field1);

        GameField field2 = mock(GameField.class);
        fields.register(field2);

        GameField field3 = mock(GameField.class);
        fields.register(field3);

        assertEquals(1, fields.id(field1));
        assertEquals(2, fields.id(field2));
        assertEquals(3, fields.id(field3));

        // when
        fields.remove(field1);

        // then
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field1));
        assertEquals(2, fields.id(field2));
        assertEquals(3, fields.id(field3));

        // when
        fields.remove(field2);

        // then
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field1));
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field2));
        assertEquals(3, fields.id(field3));

        // when
        fields.remove(field3);

        // then
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field1));
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field2));
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field3));
    }

    @Test
    public void shouldNextId_afterRemove() {
        // given
        GameField field1 = mock(GameField.class);
        fields.register(field1);
        fields.remove(field1);

        GameField field2 = mock(GameField.class);
        fields.register(field2);
        fields.remove(field2);

        // when
        GameField field3 = mock(GameField.class);
        fields.register(field3);

        // then
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field1));
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(field2));
        assertEquals(3, fields.id(field3));
    }

    @Test
    public void shouldException_ifFieldNotRegistered() {
        // when then
        assertException("IllegalStateException: Found unregistered field",
                () -> fields.id(mock(GameField.class)));
    }
}