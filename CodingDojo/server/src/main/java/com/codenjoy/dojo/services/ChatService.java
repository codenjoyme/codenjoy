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
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final Chat dao;

    public List<PMessage> getMessages(String roomId, int count, Integer afterId, Integer beforeId) {
        List<Chat.Message> messages;
        if (afterId != null && beforeId != null) {
            messages = dao.getMessagesBetweenIds(roomId, count, afterId, beforeId);
        } else if (afterId != null) {
            messages = dao.getMessagesAfterId(roomId, count, afterId);
        } else if (beforeId != null) {
            messages = dao.getMessagesBeforeId(roomId, count, beforeId);
        } else {
            messages = dao.getMessages(roomId, count);
        }
        return messages.stream()
                .map(PMessage::from)
                .collect(Collectors.toList());
    }

    public PMessage getMessage(int messageId, String roomId) {
        Chat.Message message = dao.getMessageById(messageId);
        if (message == null || !message.getRoomId().equals(roomId)) {
            throw new IllegalArgumentException(
                    "There is no message with id: " + messageId + " in room with id: " + roomId);
        }
        return PMessage.from(message);
    }

    public PMessage postMessage(String text, String roomId, Registration.User user) {
        Chat.Message message = Chat.Message.builder()
                .roomId(roomId)
                .playerId(user.getId())
                .timestamp(LocalDateTime.now())
                .text(text)
                .build();
        dao.saveMessage(message);
        return PMessage.from(message);
    }

    public boolean deleteMessage(int messageId, String roomId, Registration.User user) {
        return dao.deleteMessage(messageId, roomId, user.getId());
    }
}
