package com.codenjoy.dojo.services.controller.chat;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.utils.smart.SmartAssert;
import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static org.mockito.Mockito.*;

public class LazyChatCommandTest {

    @After
    public void after() {
        SmartAssert.checkResult();
    }

    @Test
    public void shouldCollectRequests_thenProcessByTick() {
        // given
        ChatAuthority chat = mock(ChatAuthority.class);
        List<String> errors = new LinkedList<>();
        LazyChatCommand command = new LazyChatCommand(chat, error -> errors.add(error.toString()));

        // when
        command.process(new ChatRequest("{'command':'get', " +
                "'data':{'id':1, 'room':'room1'}}"));

        command.process(new ChatRequest("{'command':'delete', " +
                "'data':{'id':2, 'room':'room2'}}"));

        command.process(new ChatRequest("{'command':'get', " +
                "'data':{'id':3, 'room':'room3'}}"));

        // then
        assertEquals("[]", errors.toString());

        verifyNoMoreInteractions(chat);

        // when
        command.tick();

        // then
        assertEquals("[]", errors.toString());

        InOrder order = inOrder(chat);
        order.verify(chat).get(1, "room1");
        order.verify(chat).delete(2, "room2");
        order.verify(chat).get(3, "room3");

        verifyNoMoreInteractions(chat);
    }

    public static class ErrorChatRequest extends ChatRequest {

        public ErrorChatRequest(String message) {
            super(message);
        }

        @Override
        public String method() {
            throw new RuntimeException("Oops!");
        }
    }

    @Test
    public void shouldCollectRequests_thenProcessByTick_caseError() {
        // given
        ChatAuthority chat = mock(ChatAuthority.class);
        List<String> errors = new LinkedList<>();
        LazyChatCommand command = new LazyChatCommand(chat, error -> errors.add(error.toString()));

        // when
        command.process(new ErrorChatRequest("{'command':'get', " +
                "'data':{'id':1, 'room':'room1'}}"));

        command.process(new ChatRequest("{'command':'delete', " +
                "'data':{'id':2, 'room':'room2'}}"));

        command.process(new ErrorChatRequest("{'command':'get', " +
                "'data':{'id':3, 'room':'room3'}}"));

        // then
        assertEquals("[]", errors.toString());

        verifyNoMoreInteractions(chat);

        // when
        command.tick();

        // then
        assertEquals("[Error(error=RuntimeException, message=Oops!), " +
                "Error(error=RuntimeException, message=Oops!)]", errors.toString());

        verify(chat, never()).get(1, "room1");
        verify(chat, times(1)).delete(2, "room2");
        verify(chat, never()).get(3, "room3");

        verifyNoMoreInteractions(chat);
    }

}
