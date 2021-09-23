package com.codenjoy.dojo.services.controller.chat;

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

import com.codenjoy.dojo.services.chat.ChatAuthority;
import com.codenjoy.dojo.services.chat.ChatType;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class ChatCommandTest {

    private ChatAuthority chat;
    private ChatCommand command;

    @Before
    public void setup() {
        // given
        chat = mock(ChatAuthority.class);
        command = new ChatCommand(chat);
    }

    @After
    public void after() {
        verifyNoMoreInteractions(chat);
    }

    @Test
    public void testGet() {
        // given
        when(chat.get(anyInt(), anyString()))
                .thenReturn(someMessage(1));

        // when
        command.process(new ChatRequest("{'command':'get', " +
                "'data':{'id':1, 'room':'room'}}"));

        // then
        verify(chat).get(1, "room");
    }

    private PMessage someMessage(int id) {
        return PMessage.from(new Chat.Message(
                "room" + id, 12 + id, ChatType.ROOM_TOPIC, "player" + id, null,
                12345L + id, "message" + id), "playerName" + id);
    }

    @Test
    public void testDelete() {
        // given
        when(chat.delete(anyInt(), anyString()))
                .thenReturn(true);

        // when
        command.process(new ChatRequest("{'command':'delete', " +
                "'data':{'id':1, 'room':'room'}}"));

        // then
        verify(chat).delete(1, "room");

    }

    @Test
    public void testAllInRoom() {
        // given
        when(chat.getAllRoom(any(Filter.class)))
                .thenReturn(Arrays.asList(someMessage(1), someMessage(2)));

        // when
        command.process(new ChatRequest("{'command':'getAllRoom', " +
                "'data':{'room':'otherRoom', 'count':1, " +
                "'afterId':2, 'beforeId':5, 'inclusive':true}}"));

        // then
        ArgumentCaptor<Filter> captor = ArgumentCaptor.forClass(Filter.class);
        verify(chat).getAllRoom(captor.capture());
        assertEquals("Filter(room=otherRoom, recipientId=null, count=1, afterId=2, " +
                        "beforeId=5, inclusive=true)",
                captor.getValue().toString());
    }

    @Test
    public void testAllInField() {
        // given
        when(chat.getAllField(any(Filter.class)))
                .thenReturn(Arrays.asList(someMessage(1), someMessage(2)));

        // when
        command.process(new ChatRequest("{'command':'getAllField', " +
                "'data':{'room':'room', 'count':10, " +
                "'afterId':3, 'beforeId':4, 'inclusive':false}}"));

        // then
        ArgumentCaptor<Filter> captor = ArgumentCaptor.forClass(Filter.class);
        verify(chat).getAllField(captor.capture());
        assertEquals("Filter(room=room, recipientId=null, count=10, afterId=3, " +
                        "beforeId=4, inclusive=false)",
                captor.getValue().toString());
    }

    @Test
    public void testAllInTopic() {
        // given
        when(chat.getAllTopic(anyInt(), any(Filter.class)))
                .thenReturn(Arrays.asList(someMessage(1), someMessage(2)));

        // when
        command.process(new ChatRequest("{'command':'getAllTopic', " +
                "'data':{'id':12, 'room':'room2', 'count':3, " +
                "'afterId':4, 'beforeId':6, 'inclusive':true}}"));

        // then
        ArgumentCaptor<Filter> captor = ArgumentCaptor.forClass(Filter.class);
        verify(chat).getAllTopic(eq(12), captor.capture());
        assertEquals("Filter(room=room2, recipientId=null, count=3, afterId=4, " +
                        "beforeId=6, inclusive=true)",
                captor.getValue().toString());
    }

    @Test
    public void testPostRoom() {
        // given
        when(chat.postRoom(anyString(), anyString()))
                .thenReturn(someMessage(1));

        // when
        command.process(new ChatRequest("{'command':'postRoom', " +
                "'data':{'text':'message4', 'room':'otherRoom'}}"));

        // then
        verify(chat).postRoom("message4", "otherRoom");
    }

    @Test
    public void testPostField() {
        // given
        when(chat.postField(anyString(), anyString()))
                .thenReturn(someMessage(1));

        // when
        command.process(new ChatRequest("{'command':'postField', " +
                "'data':{'text':'message2', 'room':'room3'}}"));

        // then
        verify(chat).postField("message2", "room3");
    }

    @Test
    public void testPostTopic() {
        // given
        when(chat.postTopic(anyInt(), anyString(), anyString()))
                .thenReturn(someMessage(1));

        // when
        command.process(new ChatRequest("{'command':'postTopic', " +
                "'data':{'id': 12, 'text':'message1', 'room':'room'}}"));

        // then
        verify(chat).postTopic(12, "message1", "room");
    }

    @Test
    public void testPostTopic_messageIsNull() {
        // when
        command.process(new ChatRequest("{'command':'postTopic', " +
                "'data':{'id': 12, 'text':null, 'room':'room'}}"));

        // then
        verify(chat).postTopic(anyInt(), eq(null), anyString());
    }

    @Test
    public void testPostTopic_caseException() {
        // given
        when(chat.postTopic(anyInt(), eq(null), anyString()))
                .thenThrow(new IllegalArgumentException("Message is null"));

        // when
        try {
            command.process(new ChatRequest("{'command':'postTopic', " +
                    "'data':{'id': 12, 'room':'room'}}"));
            fail("Expected exception");
        } catch (Exception exception) {
            assertEquals("java.lang.IllegalArgumentException: Message is null",
                    exception.toString());
        }
        // then
        verify(chat).postTopic(12, null, "room");
    }
}
