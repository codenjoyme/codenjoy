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

import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final Validator validator;
    private final Chat chat;

    public List<PMessage> getMessages(String room, int count,
                                      Integer afterId, Integer beforeId,
                                      String playerId)
    {
        validator.checkPlayerInRoom(room, playerId);

        if (afterId != null && beforeId != null) {
            return wrap(chat.getMessagesBetweenIds(room, count, afterId, beforeId));
        }

        if (afterId != null) {
            return wrap(chat.getMessagesAfterId(room, count, afterId));
        }

        if (beforeId != null) {
            return wrap(chat.getMessagesBeforeId(room, count, beforeId));
        }

        return wrap(chat.getMessages(room, count));
    }

    private List<PMessage> wrap(List<Chat.Message> messages) {
        return messages.stream()
                .map(PMessage::from)
                .collect(Collectors.toList());
    }

    public PMessage getMessage(int messageId, String room, String playerId) {
        validator.checkPlayerInRoom(room, playerId);

        Chat.Message message = chat.getMessageById(messageId);
        if (message == null || !message.getChatId().equals(room)) {
            throw new IllegalArgumentException(
                    "There is no message with id: " + messageId +
                            " in room with id: " + room);
        }
        return PMessage.from(message);
    }

    public PMessage postMessage(String text, String room, String playerId) {
        validator.checkPlayerInRoom(room, playerId);

        Chat.Message message = Chat.Message.builder()
                .chatId(room)
                .playerId(playerId)
                .time(Calendar.getInstance().getTimeInMillis())
                .text(text)
                .build();
        chat.saveMessage(message);
        return PMessage.from(message);
    }

    public boolean deleteMessage(int messageId, String room, String playerId) {
        validator.checkPlayerInRoom(room, playerId);

        return chat.deleteMessage(messageId);
    }
}
