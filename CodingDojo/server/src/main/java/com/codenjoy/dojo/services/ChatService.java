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
import com.codenjoy.dojo.services.multiplayer.GameRoom;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ChatService {

    private Validator validator;
    private Chat chat;
    private TimeService time;
    private Registration registration;
    private Spreader spreader;
    private final Map<String, String> playerNames = new ConcurrentHashMap<>();

    public List<PMessage> getMessages(String room, int count,
                                      Integer afterId, Integer beforeId,
                                      boolean inclusive,
                                      String playerId)
    {
        validateIsChatAvailable(playerId, room);

        if (afterId != null && beforeId != null) {
            return wrap(chat.getMessagesBetween(room, afterId, beforeId, inclusive));
        }

        if (afterId != null) {
            return wrap(chat.getMessagesAfter(room, count, afterId, inclusive));
        }

        if (beforeId != null) {
            return wrap(chat.getMessagesBefore(room, count, beforeId, inclusive));
        }

        return wrap(chat.getMessages(room, count));
    }

    public void validateIsChatAvailable(String playerId, String room) {
        // TODO каждый раз при загрузке страницы будет в базу идти запрос а не админ ли это? - дорого
        if (!registration.isAdmin(playerId)) {
            validator.checkPlayerInRoom(playerId, room);
        }
    }

    private List<PMessage> wrap(List<Chat.Message> messages) {
        return messages.stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    private PMessage wrap(Chat.Message message) {
        return PMessage.from(message,
                playerName(message.getPlayerId()));
    }

    private String playerName(String playerId) {
        if (!playerNames.containsKey(playerId)) {
            playerNames.put(playerId, registration.getNameById(playerId));
        }
        return playerNames.get(playerId);
    }

    public List<PMessage> getTopicMessages(int topicMessageId, String room, String playerId) {
        validateIsChatAvailable(playerId, room);
        validateTopicExists(topicMessageId, room);

        return wrap(chat.getTopicMessages(topicMessageId));
    }

    private void validateTopicExists(int topicMessageId, String room) {
        // TODO по сути будет по 2 запроса, что не ок по производительности
        //      можно было бы валидацию зашить во второй запрос?
        // room validation only
        getMessage(topicMessageId, room);
    }

    public List<PMessage> getFieldMessages(String room, String playerId) {
        validateIsChatAvailable(playerId, room);
        // TODO test me
        int topicId = getFieldTopicId(room, playerId);

        return wrap(chat.getTopicMessages(topicId));
    }

    private int getFieldTopicId(String room, String playerId) {
        Optional<GameRoom> gameRoom = spreader.gameRoom(room, playerId);
        gameRoom.orElseThrow(() ->
                exception("There is no player '%s' in room '%s'",
                        playerId, room));

        int fieldId = gameRoom.get().field().id();
        int topicId = -fieldId; // TODO а точно тут надо минус, может сделать строковую айдишку скажем F-34324?
        return topicId;
    }

    public PMessage getMessage(int messageId, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        return getMessage(messageId, room);
    }

    private PMessage getMessage(int messageId, String room) {
        Chat.Message message = chat.getMessageById(messageId);

        if (message == null || !message.getRoom().equals(room)) {
            throw exception("There is no message with id '%s' in room '%s'",
                    messageId, room);
        }
        return wrap(message);
    }

    public PMessage postMessageForField(String text, String room, String playerId) {
        validateIsChatAvailable(playerId, room);
        // TODO test me
        int topicId = getFieldTopicId(room, playerId);

        return saveMessage(topicId, text, room, playerId);
    }

    public PMessage postMessage(Integer topicMessageId, String text, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        if (topicMessageId != null) {
            validateTopicExists(topicMessageId, room);
        }

        return saveMessage(topicMessageId, text, room, playerId);
    }

    private PMessage saveMessage(Integer topicId, String text, String room, String playerId) {
        return wrap(chat.saveMessage(
                Chat.Message.builder()
                        .room(room)
                        .topicId(topicId)
                        .playerId(playerId)
                        .time(time.now())
                        .text(text)
                        .build()));
    }

    public boolean deleteMessage(int messageId, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        boolean deleted = chat.deleteMessage(room, messageId, playerId);

        if (!deleted) {
            throw exception("Player '%s' cant delete message with id '%s' in room '%s'",
                    playerId, messageId, room);
        }

        return true;
    }

    public IllegalArgumentException exception(String message, Object... parameters) {
        return new IllegalArgumentException(String.format(message, parameters));
    }
}
