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
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.ChatType;
import static com.codenjoy.dojo.services.chat.ChatType.*;
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

    public Chat.Message addMessage(String room, String player, Integer topicId, ChatType type) {
        return ChatTest.addMessage(chat, messages, room, player, topicId, type);
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
}