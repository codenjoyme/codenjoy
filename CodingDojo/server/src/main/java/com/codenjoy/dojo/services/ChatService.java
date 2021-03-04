package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.dao.ChatDAO;
import com.codenjoy.dojo.web.rest.pojo.PChatHistory;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatDAO dao;

    public PChatHistory getConversation(String roomId, LocalDateTime fromTime, int size) {
        List<PMessage> messages = dao.getMessages(roomId, fromTime, size);
        LocalDateTime next = null;
        int messagesCount = messages.size();
        if (messagesCount > size) {
            next = messages.get(messagesCount - 1).getTimestamp();
            messages.remove(messagesCount - 1);
        }
        LocalDateTime previous = null;
        PMessage messageBefore = dao.getMessageBefore(roomId, fromTime);
        if (messageBefore != null) {
            previous = messageBefore.getTimestamp();
        }
        return new PChatHistory(previous, next, messages);
    }

    public PMessage postMessage(String roomId, String playerId, String text) {
        PMessage message = PMessage.builder()
                .id(null)
                .text(text)
                .roomId(roomId)
                .playerId(playerId)
                .timestamp(LocalDateTime.now())
                .build();
        return dao.saveMessage(message);
    }

    public boolean deleteMessage(String messageId) {
        return dao.deleteMessage(messageId);
    }
}
