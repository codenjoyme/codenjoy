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

import java.util.Arrays;
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
     * {@code playerId} в room/filed/topic-чате (в соответствии с {@code type})
     * конкретной комнаты {@code room}.
     *
     * Администратор может получать сообщения в любом чате,
     * пользователь - только в своем чате {@code room}.
     */
    private List<PMessage> getMessages(ChatType type, Integer topicId,
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
     * Метод для получения всех сообщений для конкретного пользователя
     * {@code playerId} в room-чате {@code room}.
     *
     * Администратор может получать любые сообщения в любом room-чате,
     * пользователь только своего в своем room-чате.
     */
    public List<PMessage> getRoomMessages(String playerId, Filter filter) {
        validateIsChatAvailable(playerId, filter.room());

        return getMessages(ROOM, null, playerId, filter);
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
     * пользователь только topic-сообщения в своем room-чате.
     */
    public List<PMessage> getTopicMessages(int topicId, String playerId, Filter filter) {
        validateIsChatAvailable(playerId, filter.room());
        validateTopicExists(topicId, filter.room());

        return getMessages(TOPIC, topicId, playerId, filter);
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
    public List<PMessage> getFieldMessages(String playerId, Filter filter) {
        int topicId = getFieldTopicId(filter.room(), playerId);
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
     * Метод для получения конкретного сообщения по {@code id}
     * из комнаты {@code room}. С его помощью можно получать любые сообщения
     * из любого чата (room, tread или field).
     *
     * Администратор может получать сообщения из любой {@code room},
     * пользователь только сообщения из своей комнаты.
     */
    // TODO пользователь зная fieldId не своей борды, и id сообщения там
    //      сможет получить его с помощью этого метода
    public PMessage getMessage(int id, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        return getMessage(id, room);
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
     * Метод для публикации сообщения в topic-чат комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь находится в данной комнате.
     */
    public PMessage postMessageForTopic(int id, String text, String room, String playerId) {
        return postMessage(TOPIC, id, text, room, playerId);
    }

    /**
     * Метод для публикации сообщения в room-чат комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь находится в данной комнате.
     */
    public PMessage postMessageForRoom(String text, String room, String playerId) {
        return postMessage(ROOM, null, text, room, playerId);
    }

    /**
     * Метод для публикации сообщения в room-чат (или thread-чат,
     * если указан {@code topicId}) комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь находится в данной комнате.
     */
    private PMessage postMessage(ChatType type, Integer topicId,
                                String text, String room, String playerId)
    {
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
     * Метод для удаления сообщения {@code id} в любом чате
     * (room-чат, thread-чат или field-чат) комнаты {@code room}
     * от имени пользователя {@code playerId}.
     *
     * Это возможно только, если пользователь является автором сообщения
     * и продолжает пребывать в заданной комнате (при этом при удалении
     * field-сообщений он может покинуть изначальную field и пребывать в дургой)
     */
    public boolean deleteMessage(int id, String room, String playerId) {
        validateIsChatAvailable(playerId, room);

        boolean deleted = chat.deleteMessage(room, id, playerId);

        if (!deleted) {
            throw exception("Player '%s' cant delete message with id '%s' in room '%s'",
                    playerId, id, room);
        }

        return true;
    }

    private Chat.Message rootFor(Chat.Message message) {
        while (message.getType() == TOPIC) {
            Integer topicId = message.getTopicId();
            if (topicId == null) {
                throw exception("Topic message with null topic_id: " + message.getId());
            }
            message = chat.getAnyMessageById(topicId);
            if (message == null) {
                return null;
            }
        }
        return message;
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

    public ChatControl control(String playerId, ChatControl.OnChange listener) {
        return new ChatControl() {
            @Override
            public List<PMessage> getAllRoom(Filter filter) {
                return getRoomMessages(playerId, filter);
            }

            @Override
            public List<PMessage> getAllTopic(int topicId, Filter filter) {
                return getTopicMessages(topicId, playerId, filter);
            }

            @Override
            public List<PMessage> getAllField(Filter filter) {
                return getFieldMessages(playerId, filter);
            }

            @Override
            public PMessage get(int id, String room) {
                PMessage message = getMessage(id, room, playerId);
                informCreateForPlayer(Arrays.asList(message), playerId, listener);
                return message;
            }

            @Override
            public PMessage postRoom(String text, String room) {
                PMessage message = postMessageForRoom(text, room, playerId);
                informCreateInRoom(Arrays.asList(message), room, listener);
                return message;
            }

            private void informCreateForPlayer(List<PMessage> messages, String playerId, OnChange listener) {
                listener.created(messages, playerId);
            }

            private void informCreateInRoom(List<PMessage> messages, String room, OnChange listener) {
                spreader.players(room)
                        .forEach(player -> listener.created(messages, player.getId()));
            }

            private void informCreateInField(List<PMessage> messages, int fieldId, OnChange listener) {
                spreader.players(fieldId)
                        .forEach(player -> listener.created(messages, player.getId()));
            }

            private void informDeleteInRoom(List<PMessage> messages, String room, OnChange listener) {
                spreader.players(room)
                        .forEach(player -> listener.deleted(messages, player.getId()));
            }

            private void informDeleteInField(List<PMessage> messages, int fieldId, OnChange listener) {
                spreader.players(fieldId)
                        .forEach(player -> listener.deleted(messages, player.getId()));
            }

            @Override
            public PMessage postTopic(int topicId, String text, String room) {
                PMessage message = postMessageForTopic(topicId, text, room, playerId);
                informCreateInRoom(Arrays.asList(message), room, listener);
                return message;
            }

            @Override
            public PMessage postField(String text, String room) {
                PMessage message = postMessageForField(text, room, playerId);
                informCreateInField(Arrays.asList(message), message.getTopicId(), listener);
                return message;
            }

            @Override
            public boolean delete(int id, String room) {
                // TODO тут несколько запросов делается, не совсем оптимально
                Chat.Message message = chat.getMessageById(id);
                boolean deleted = deleteMessage(id, room, playerId);
                if (deleted) {
                    informDelete(wrap(message), room, rootFor(message));
                }
                return deleted;
            }

            private void informDelete(PMessage message, String room, Chat.Message root) {
                ChatType type = (root == null)
                        // TODO очень мало вероятно что такое случится,
                        //   но если так - то информируем всех в комнате
                        ? ROOM
                        : root.getType();

                switch (type) {
                    case ROOM:
                        informDeleteInRoom(Arrays.asList(message), room, listener);
                        break;
                    case FIELD:
                        informDeleteInField(Arrays.asList(message), root.getTopicId(), listener);
                        break;
                    default:
                        throw exception("Should be only ROOM or FIELD: " + message.getId());
                }
            }
        };
    }

}
