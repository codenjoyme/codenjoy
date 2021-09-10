package com.codenjoy.dojo.services.helper;

import com.codenjoy.dojo.services.chat.ChatType;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.ChatTest;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.chat.ChatType.ROOM;
import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ChatHelper {

    private final Chat chat;
    private List<Chat.Message> messages = new LinkedList<>();

    public void removeAll() {
        chat.removeAll();
        messages.clear();
    }

    public Chat.Message post(String room, String player, Integer topicId, ChatType type) {
        long time = JDBCTimeUtils.getTimeLong("2021-03-08T21:23:43.345+0200");
        int index = messages.size() + 1;
        Chat.Message message = new Chat.Message(
                room, topicId, type, player,
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
}
