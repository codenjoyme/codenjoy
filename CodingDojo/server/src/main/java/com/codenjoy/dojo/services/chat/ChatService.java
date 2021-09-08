package com.codenjoy.dojo.services.chat;

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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.FieldService;
import com.codenjoy.dojo.services.TimeService;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.multiplayer.GameRoom;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.services.chat.ChatType.*;

@Service
@AllArgsConstructor
public class ChatService {

    private Validator validator;
    private Chat chat;
    private TimeService time;
    private Registration registration;
    private Spreader spreader;
    private FieldService fields;
    private final Map<String, String> playerNames = new ConcurrentHashMap<>();

    /**
     * Метод для получения заданного количества сообщений (относительно конкретных
     * {@code afterId}/{@code beforeId} сообщений) для конкретного пользователя
     * {@code playerId} в room-чате {@code room}
     * (или топика в нем, если указан {@param topicId}),
     *
     * Администратор может получать сообщения в любом чате,
     * пользователь - только в своем чате {@code room}.
     */
    public List<PMessage> getMessages(ChatType type, Integer topicId,
                                      String playerId, Filter filter)
    {
        validateIsChatAvailable(playerId, filter.room());

        if (filter.afterId() != null && filter.beforeId() != null) {
            return wrap(chat.getMessagesBetween(type, topicId, filter));
        }

        if (filter.afterId() != null) {
            return wrap(chat.getMessagesAfter(topicId, type, filter));
        }

        if (filter.beforeId() != null) {
            return wrap(chat.getMessagesBefore(type, topicId, filter));
        }

        return wrap(chat.getMessages(type, topicId, filter));
    }

    /**
     * Проверка пройдет, если плеер {@code playerId} находится
     * в заданной комнате {@code room} или если он админ.
     */
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

    /**
     * Метод для получения всех сообщений для конкретного пользователя
     * {@code playerId} в topic-чате (чате под конкретным
     * {@code topicMessageId} сообщением room-чата {@code room}).
     *
     * Администратор может получать любые topic-сообщения в любом room-чате,
     * пользователь только topic-чообщения в своем room-чате.
     */
    public List<PMessage> getTopicMessages(int topicId, String room, String playerId) {
        validateIsChatAvailable(playerId, room);
        validateTopicExists(topicId, room);

        return wrap(chat.getTopicMessages(topicId));
    }

    private void validateTopicExists(int id, String room) {
        // TODO по сути будет по 2 запроса, что не ок по производительности
        //      можно было бы валидацию зашить во второй запрос?
        // room validation only
        getMessage(id, room);
    }

    /**
     * Метод для получения заданного количества сообщений (относительно конкретных
     * {@code afterId}/{@code beforeId} сообщений) для конкретного пользователя
     * {@code playerId} в field-чате поля на котором он играет в комнате
     * {@code room}.
     *
     * Администратор не может получать field-чат сообщения,
     * пользователь - только сообщения field-чата поля на котором пока что играет.
     */
    public List<PMessage> getFieldMessages(String room, String playerId, Filter filter) {
        int topicId = getFieldTopicId(room, playerId);
        return getMessages(FIELD, topicId, playerId, filter);
    }

    /**
     * Метод получения fieldId поля на котором играет пользователь {@code playerId}
     * в комнате {@code room} c  предварительной проверкой
     * соответствия пользователя комнате.
     */
    private int getFieldTopicId(String room, String playerId) {
        Optional<GameRoom> gameRoom = spreader.gameRoom(room, playerId);
        gameRoom.orElseThrow(() ->
                exception("There is no player '%s' in room '%s'",
                        playerId, room));

        return fields.id(gameRoom.get().field());
    }

    /**
     * Метод для получения конкретного сообщения по {@code topicId}
     * из комнаты {@code room}. С его помощью можно получать любые сообщения
     * из любого чата (room, tread или field).
     *
     * Администратор может получать сообщения из любой {@code room},
     * пользователь только сообщения из своей комнаты.
     */
    // TODO пользователь зная fieldId не своей борды, и id сообщения там
    //      сможет получить его с помощью этого метода
    public PMessage getMessage(int topicId, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        return getMessage(topicId, room);
    }

    private PMessage getMessage(int id, String room) {
        Chat.Message message = chat.getMessageById(id);

        if (message == null || !message.getRoom().equals(room)) {
            throw exception("There is no message with id '%s' in room '%s'",
                    id, room);
        }
        return wrap(message);
    }

    /**
     * Метод для публикации сообщения в field-чат комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь находится в данной комнате.
     */
    public PMessage postMessageForField(String text, String room, String playerId) {
        int topicId = getFieldTopicId(room, playerId);
        return saveMessage(topicId, FIELD, text, room, playerId);
    }

    /**
     * Метод для публикации сообщения в room-чат (или thread-чат,
     * если указан {@code topicId}) комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь находится в данной комнате.
     */
    public PMessage postMessage(Integer topicId, ChatType type, String text, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        if (topicId != null) {
            validateTopicExists(topicId, room);
        }

        return saveMessage(topicId, type, text, room, playerId);
    }

    private PMessage saveMessage(Integer topicId, ChatType type, String text, String room, String playerId) {
        return wrap(chat.saveMessage(
                Chat.Message.builder()
                        .room(room)
                        .topicId(topicId)
                        .type(type)
                        .playerId(playerId)
                        .time(time.now())
                        .text(text)
                        .build()));
    }

    /**
     * Метод для удаления сообщения {@code messageId} в любом чате
     * (room-чат, thread-чат или field-чат) комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь является автором сообщения
     * и продолжает пребывать в заданной комнате (при этом при удалении
     * field-сообщений он может покинуть изначальную field и пребывать в дургой)
     */
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

    @Data
    @AllArgsConstructor
    @ToString
    public static class Status {
        private int fieldId;
        private Integer lastInRoom;
        private Integer lastInField;
        // TODO доделать и вернуть все
        // private Map<Integer, Integer> lastInTopic;
    }

    @ToString
    public class LastMessage {
        private Map<String, Integer> room;
        private Map<Integer, Integer> topic;
        private Map<Integer, Integer> field;

        public LastMessage() {
            room = chat.getLastRoomMessageIds();
            topic = chat.getLastTopicMessageIds(TOPIC);
            field = chat.getLastTopicMessageIds(FIELD);
        }

        public Status at(Deal deal) {
            int fieldId = fields.id(deal.getField());
            return new Status(
                    fieldId,
                    room.get(deal.getRoom()),
                    field.get(fieldId));
        }
    }

    public LastMessage getLast() {
        return new LastMessage();
    }
}
