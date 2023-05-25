package com.codenjoy.dojo.services.helper;

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

import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.chat.ChatType;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.ChatTest;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.client.Utils.split;
import static com.codenjoy.dojo.services.chat.ChatType.ROOM;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;
import static java.util.stream.Collectors.toList;

@Lazy
@Component
@RequiredArgsConstructor
public class ChatHelper {

    private final Chat chat;
    private final ChatService chatService;
    private List<Chat.Message> messages = new LinkedList<>();

    public void removeAll() {
        chat.removeAll();
        messages.clear();
    }

    public Chat.Message post(String room, String player, Integer topicId, ChatType type) {
        return post(room, player, topicId, type, null);
    }

    public Chat.Message post(String room, String player, Integer topicId, ChatType type, String recipient) {
        long time = JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200");
        int index = messages.size() + 1;
        Chat.Message message = new Chat.Message(
                room, topicId, type,
                player, recipient,
                time + 100000L * index,
                "message" + index);
        Chat.Message added = chat.saveMessage(message);
        messages.add(added);
        return added;
    }

    public Chat.Message post(String room, String player) {
        return post(room, player, null, ROOM);
    }

    public AssertThat assertThat(int... ids) {
        return new AssertThat(ids);
    }

    public void removeAll(ChatType type, Integer topicId, String room, String expected) {
        List<Chat.Message> list = chat.getMessages(type, topicId,
                Filter.room(room)
                        .count(Integer.MAX_VALUE)
                        .get());

        assertEquals(expected, list.toString()); // TODO add split /n

        list.forEach(message ->
                assertEquals(true, chat.deleteMessage(room,
                        message.getId(), message.getPlayerId())));
    }

    public void checkField(String player, String room, String expected) {
        check(getField(player, room), expected);
    }

    public List<PMessage> getField(String player, String room) {
        return chatService.getFieldMessages(player,
                Filter.room(room)
                        .count(Integer.MAX_VALUE)
                        .get());
    }

    public void cleanField(String player, String room) {
        getField(player, room)
                .forEach(message ->
                        chatService.deleteMessage(message.getId(),
                                message.getRoom(),
                                message.getPlayerId()));
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

            List<Chat.Message> expected = ChatHelper.this.messages.stream()
                    .filter(message -> isPresent(actual, message))
                    .collect(toList());

            assertEquals(ChatTest.toString(expected),
                    ChatTest.toString(actual));
        }

        public boolean isPresent(List<Chat.Message> list, Chat.Message found) {
            return list.stream()
                    .anyMatch(message -> found.getId() == message.getId());
        }
    }

    public void check(PMessage message, String expected) {
        check(Arrays.asList(message), expected);
    }

    public void check(List<PMessage> messages, String expected) {
        assertEquals(expected,
                split(messages,
                        ", \n\tplayerId",
                        "), \nPMessage("));
    }
}
