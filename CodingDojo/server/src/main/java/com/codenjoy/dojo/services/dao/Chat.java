package com.codenjoy.dojo.services.dao;

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

import com.codenjoy.dojo.services.chat.ChatType;
import com.codenjoy.dojo.services.chat.Filter;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudPrimaryKeyConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.lang.String.format;

public class Chat {

    // TODO рассмотреть возможность постить личные сообщения в topic и
    //      room чате во всех api.
    public static final String FOR_ALL = null;

    public static final int MESSAGE_MAX_LENGTH = 12000;

    private final CrudPrimaryKeyConnectionThreadPool pool;

    public Chat(ConnectionThreadPoolFactory factory) {
        // TODO replace all varchar(255) in all dao into more suitable types (postgres + sqlite)
        pool = (CrudPrimaryKeyConnectionThreadPool) factory.create(
                "CREATE TABLE IF NOT EXISTS messages (" +
                    "id integer_primary_key, " +
                    "room varchar(255), " +
                    "topic_id int, " +
                    "type int, " +
                    "deleted boolean, " +
                    "player_id varchar(255), " +
                    "recipient_id varchar(255), " +
                    "time varchar(255), " +
                    "text varchar(" + MESSAGE_MAX_LENGTH + "));"
        );
    }

    public void removeDatabase() {
        pool.removeDatabase();
    }

    /**
     * @return {@param count} последних сообщений
     *        для текущего чата {@param room}
     *        (или дочернего чата в нем, если указан {@param topicId}),
     *        отсортированных в порядке возрастания времени.
     */
    public List<Message> getMessages(ChatType type, Integer topicId, Filter filter) {
        return pool.select(
                "SELECT * " +
                    "FROM " +
                        "(SELECT * FROM messages " +
                            "WHERE deleted = FALSE " +
                                "AND room = ? " +
                                "AND " + is("topic_id", topicId) +
                                "AND " + nullOrIs("recipient_id", filter.recipientId()) +
                                "AND type = ? " +
                            "ORDER BY time DESC " +
                            "LIMIT ?) as result " +
                    "ORDER BY time ASC;",
                new Object[]{
                        filter.room(),
                        type.id(),
                        filter.count()
                },
                Chat::parseMessages
        );
    }

    /**
     * @return Последнюю fieldId которая была зарегистрирована в прошлом.
     */
    public int getLastFieldId() {
        return pool.select(
                "SELECT topic_id " +
                    "FROM messages " +
                    "WHERE type = ? " +
                    "ORDER BY topic_id DESC " +
                    "LIMIT 1;",
                new Object[]{
                        ChatType.FIELD.id()
                },
                rs -> rs.next() ? rs.getInt(1) : 0);
    }

    public Integer getLastMessageId(String room) {
        return pool.select(
                "SELECT id " +
                    "FROM messages " +
                    "WHERE deleted = FALSE " +
                        "AND room = ? " +
                        "AND type = ? " +
                    "ORDER BY time DESC " +
                    "LIMIT 1;",
                new Object[]{
                        room,
                        ChatType.ROOM.id()
                },
                rs -> rs.next() ? rs.getInt(1) : null);
    }

    /**
     * Используется для информирования пользователя о том, что
     * пришли новые сообщения в room-чат. Метод готовит данные
     * сразу для всех комнат.
     *
     * @return Возвращает последние сообщения в каждой комнате
     * с ключом этой комнаты, а значением - id сообщения.
     */
    public Map<String, Integer> getLastRoomMessageIds() {
        return (Map) pool.select(
                "SELECT m2.room AS key, m2.id AS value " +
                    "FROM " +
                        "(SELECT room, MAX(time) AS time " +
                            "FROM messages " +
                            "WHERE deleted = FALSE " +
                                "AND type = ? " +
                            "GROUP BY room) m1 " +
                    "JOIN messages m2 " +
                        "ON m1.room = m2.room " +
                            "AND m1.time = m2.time " +
                    "ORDER BY m2.time ASC;",
                new Object[]{
                        ChatType.ROOM.id()
                },
                rs -> toMap(rs));
    }

    /**
     * Используется для информирования пользователя о том, что
     * пришли новые сообщения в topic или field-чат. Метод готовит данные
     * сразу для всех topic и filed.
     *
     * @param type тип чата идентификаторами которого интересуемся.
     * @return Возвращает последние сообщения в выбранном чате.
     * Ключ для topic-чата topicMessageId, а для field-чата ключ fieldId.
     * Значение - id последнего сообщения в этом чате.
     */
    public Map<Integer, Integer> getLastTopicMessageIds(ChatType type) {
        return (Map) pool.select(
                "SELECT m2.topic_id AS key, m2.id AS value " +
                    "FROM " +
                        "(SELECT topic_id, MAX(time) AS time " +
                            "FROM messages " +
                            "WHERE deleted = FALSE " +
                                "AND type = ? " +
                            "GROUP BY topic_id) m1 " +
                    "JOIN messages m2 " +
                        "ON m1.topic_id = m2.topic_id " +
                            "AND m1.time = m2.time " +
                    "ORDER BY m2.time ASC;",
                new Object[]{
                        type.id()
                },
                rs -> toMap(rs));
    }

    @SneakyThrows
    public Map<Object, Integer> toMap(ResultSet rs) {
        return new LinkedHashMap<>(){{
           while (rs.next()) {
                put(rs.getObject("key"), rs.getInt("value"));
           }
        }};
    }

    /**
     * @return Все сообщения текущего топика (родительского сообщения) {@param topicId},
     *        отсортированных в порядке возрастания времени
     */
    public List<Message> getTopicMessages(ChatType type, int topicId) {
        return pool.select(
                "SELECT * FROM messages " +
                    "WHERE deleted = FALSE " +
                        "AND " + is("topic_id", topicId) +
                        "AND type = ? " +
                    "ORDER BY time ASC;",
                new Object[]{
                        type.id()
                },
                Chat::parseMessages
        );
    }

    /**
     * @return Все сообщения в диапазоне ({@param afterId}...{@param beforeId})
     *        для текущего чата {@param room}
     *        (или дочернего чата в нем, если указан {@param topicId}),
     *        отсортированных в порядке возрастания времени.
     *        Если флаг {@param inclusive} установлен - ты получишь так же в запросе
     *        message {@param afterId} и message {@param beforeId} помимо выбранных.
     */
    public List<Message> getMessagesBetween(ChatType type, Integer topicId, Filter filter){
        if (filter.afterId() > filter.beforeId()) {
            throw new IllegalArgumentException(
                    "afterId in interval should be smaller than beforeId");
        }
        return pool.select(
                "SELECT * FROM messages " +
                    "WHERE deleted = FALSE " +
                        "AND room = ? " +
                        "AND " + is("topic_id", topicId) +
                        "AND " + nullOrIs("recipient_id", filter.recipientId()) +
                        "AND type = ? " +
                        "AND id >" + eq(filter) + " ? " +
                        "AND id <" + eq(filter) + " ? " +
                    "ORDER BY time ASC;",
                new Object[]{
                        filter.room(),
                        type.id(),
                        filter.afterId(),
                        filter.beforeId()
                },
                Chat::parseMessages
        );
    }

    private String is(String column, Integer id) {
        return (id == null)
                ? column + " IS NULL "
                : column + " = '" + id + "' ";
    }

    private String nullOrIs(String column, String id) {
        return (id == null)
                ? format("%s IS NULL ", column)
                : format("(%s IS NULL OR %s = '%s') ", column, column, id);
    }

    private String eq(Filter filter) {
        return filter.inclusive() ? "=" : "";
    }

    /**
     * @return {@param count} первых сообщений начиная с {@param afterId}
     *        для текущего чата {@param room}
     *        (или дочернего чата в нем, если указан {@param topicId}),
     *        отсортированных в порядке возрастания времени.
     *        Если флаг {@param inclusive} установлен - ты получишь так же в запросе
     *        message {@param afterId}.
     */
    public List<Message> getMessagesAfter(ChatType type, Integer topicId, Filter filter) {
        return pool.select(
                "SELECT * FROM messages " +
                    "WHERE deleted = FALSE " +
                        "AND room = ? " +
                        "AND " + is("topic_id", topicId) +
                        "AND " + nullOrIs("recipient_id", filter.recipientId()) +
                        "AND type = ? " +
                        "AND id >" + eq(filter) + " ? " +
                    "ORDER BY time ASC " +
                    "LIMIT ?;",
                new Object[]{
                        filter.room(),
                        type.id(),
                        filter.afterId(),
                        filter.count()
                },
                Chat::parseMessages
        );
    }

    /**
     * @return {@param count} последних сообщений перед {@param beforeId}
     *        для текущего чата {@param room}
     *        (или дочернего чата в нем, если указан {@param topicId}),
     *        отсортированных в порядке возрастания времени.
     *        Если флаг {@param inclusive} установлен - ты получишь так же в запросе
     *        message {@param beforeId}.
     */
    public List<Message> getMessagesBefore(ChatType type, Integer topicId, Filter filter) {
        return pool.select(
                "SELECT * " +
                    "FROM " +
                        "(SELECT * FROM messages " +
                            "WHERE deleted = FALSE " +
                                "AND room = ? " +
                                "AND " + is("topic_id", topicId) +
                                "AND " + nullOrIs("recipient_id", filter.recipientId()) +
                                "AND type = ? " +
                                "AND id <" + eq(filter) + " ? " +
                            "ORDER BY time DESC " +
                            "LIMIT ?) as result " +
                    "ORDER BY time ASC;",
                new Object[]{
                        filter.room(),
                        type.id(),
                        filter.beforeId(),
                        filter.count()
                },
                Chat::parseMessages
        );
    }

    public Message getMessageById(int messageId) {
        return pool.select(
                "SELECT * " +
                    "FROM messages " +
                    "WHERE deleted = FALSE " +
                        "AND id = ?",
                new Object[]{messageId},
                rs -> rs.next() ? new Message(rs) : null
        );
    }

    /**
     * @param messageId Искомое сообщение.
     * @return Найденное сообщение, даже если оно было ранее удалено.
     */
    public Message getAnyMessageById(int messageId) {
        return pool.select(
                "SELECT * " +
                    "FROM messages " +
                    "WHERE id = ?",
                new Object[]{messageId},
                rs -> rs.next() ? new Message(rs) : null
        );
    }

    public ChatType getTypeById(int messageId) {
        return pool.select(
                "SELECT type " +
                    "FROM messages " +
                    "WHERE id = ?",
                new Object[]{messageId},
                rs -> rs.next() ? type(rs) : null
        );
    }

    private static ChatType type(ResultSet rs) throws SQLException {
        return ChatType.valueOf(rs.getInt("type"));
    }

    // TODO test deleted
    public Message saveMessage(Message message) {
        List<Object> objects = pool.batch(Arrays.asList(
                "INSERT INTO messages " +
                        "(room, deleted, topic_id, type, player_id, recipient_id, time, text) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?);",
                pool.getLastInsertedIdQuery("messages", "id")),

                Arrays.asList(new Object[]{
                                message.getRoom(),
                                false,
                                message.getTopicId(),
                                message.getType().id(),
                                message.getPlayerId(),
                                message.getRecipientId(),
                                JDBCTimeUtils.toString(new Date(message.getTime())),
                                message.getText()
                        },
                        new Object[]{}),

                Arrays.asList(null,
                        rs -> rs.next() ? rs.getInt(1) : null));


        message.setId((Integer) objects.get(1));
        return message;
    }

    public boolean deleteMessage(String room, int id, String playerId) {
        int count = pool.update(
                "UPDATE messages " +
                    "SET deleted = TRUE " +
                    "WHERE id = ? " +
                        "AND room = ? " +
                        "AND player_id = ?",
                new Object[]{
                        id,
                        room,
                        playerId
                });
        return count == 1;
    }

    private static List<Message> parseMessages(ResultSet rs) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            messages.add(new Message(rs));
        }
        return messages;
    }

    public void removeAll() {
        pool.clearLastInsertedId("messages", "id");
        pool.update("DELETE FROM messages");
    }

    @Data
    public static class Message {
        private int id;
        private Integer topicId;
        private ChatType type;
        private String room;
        private String playerId;
        private String recipientId;
        private long time;
        private String text;

        @Builder
        public Message(String room, Integer topicId, ChatType type, String playerId, String recipientId, long time, String text) {
            this.room = room;
            this.topicId = topicId;
            this.type = type;
            this.playerId = playerId;
            this.recipientId = recipientId;
            this.time = time;
            this.text = text;
        }

        private Message(ResultSet rs) throws SQLException {
            id = rs.getInt("id");
            room = rs.getString("room");
            topicId = (Integer) rs.getObject("topic_id");
            type = type(rs);
            playerId = rs.getString("player_id");
            recipientId = rs.getString("recipient_id");
            time = JDBCTimeUtils.getTimeLong(rs);
            text = rs.getString("text");
        }
    }
}
