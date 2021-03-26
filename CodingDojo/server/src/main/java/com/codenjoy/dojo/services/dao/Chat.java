package com.codenjoy.dojo.services.dao;

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

public class Chat {
    private final CrudPrimaryKeyConnectionThreadPool pool;

    public Chat(ConnectionThreadPoolFactory factory) {
        // TODO replace all varchar(255) in all dao into more suitable types (postgres + sqlite)
        pool = (CrudPrimaryKeyConnectionThreadPool) factory.create(
                "CREATE TABLE IF NOT EXISTS messages (" +
                        "id integer_primary_key, " +
                        "room varchar(255), " +
                        "topic_id int, " +
                        "player_id varchar(255), " +
                        "time varchar(255), " +
                        "text varchar(255));"
        );
    }

    public void removeDatabase() {
        pool.removeDatabase();
    }

    /**
     * @return {@param count} последних сообщений
     *        для текущего чата {@param room},
     *        посортированных в порядке возрастания времени
     */
    public List<Message> getMessages(String room, int count) {
        return pool.select("SELECT * FROM " +
                        "(SELECT * FROM messages " +
                        "WHERE room = ? " +
                        "AND topic_id IS NULL " +
                        "ORDER BY time DESC " +
                        "LIMIT ?) as result " +
                        "ORDER BY time ASC;",
                new Object[]{room, count},
                Chat::parseMessages
        );
    }

    public Integer getLastMessageId(String room) {
        return pool.select("SELECT id " +
                        "FROM messages " +
                        "WHERE room = ? " +
                        "ORDER BY time DESC " +
                        "LIMIT 1;",
                new Object[]{room},
                rs -> rs.next() ? rs.getInt(1) : null);
    }

    public Map<String, Integer> getLastMessageIds() {
        return pool.select("SELECT room, id, MAX(time) as max_time " +
                        "FROM messages " +
                        "GROUP BY room, id " +
                        "ORDER BY room ASC;",
                new Object[]{},
                rs -> toMap(rs));
    }

    @SneakyThrows
    public Map<String, Integer> toMap(ResultSet rs) {
        return new LinkedHashMap<>(){{
           while (rs.next()) {
                put(rs.getString("room"), rs.getInt("id"));
           }
        }};
    }

    /**
     * @return Все соообщения текущего топика (родительского сообщения) {@param messageId},
     *        посортированных в порядке возрастания времени
     */
    public List<Message> getTopicMessages(int messageId) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE topic_id = ? " +
                        "ORDER BY time ASC;",
                new Object[]{messageId},
                Chat::parseMessages
        );
    }

    /**
     * @return все сообщения в диапазоне ({@param afterId}...{@param beforeId})
     *        для текущего чата {@param room},
     *        посортированных в порядке возрастания времени.
     *        Если флаг {@param inclusive} установлен - ты получишь так же в запросе
     *        message {@param afterId} и message {@param beforeId} помимо выбранных.
     */
    public List<Message> getMessagesBetween(String room,
                                            int afterId, int beforeId,
                                            boolean inclusive)
    {
        if (afterId > beforeId) {
            throw new IllegalArgumentException("afterId in interval should be smaller than beforeId");
        }
        return pool.select("SELECT * FROM messages " +
                        "WHERE room = ? " +
                        "AND topic_id IS NULL " +
                        "AND id >" + (inclusive?"=":"") + " ? " +
                        "AND id <" + (inclusive?"=":"") + " ? " +
                        "ORDER BY time ASC;",
                new Object[]{room, afterId, beforeId},
                Chat::parseMessages
        );
    }

    /**
     * @return {@param count} первых сообщений начиная с {@param afterId}
     *        для текущего чата {@param room},
     *        посортированных в порядке возрастания времени.
     *        Если флаг {@param inclusive} установлен - ты получишь так же в запросе
     *        message {@param afterId}.
     */
    public List<Message> getMessagesAfter(String room, int count,
                                          int afterId, boolean inclusive)
    {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room = ? " +
                        "AND topic_id IS NULL " +
                        "AND id >" + (inclusive?"=":"") + " ? " +
                        "ORDER BY time ASC " +
                        "LIMIT ?;",
                new Object[]{room, afterId, count},
                Chat::parseMessages
        );
    }

    /**
     * @return {@param count} последних сообщений перед {@param beforeId}
     *        для текущего чата {@param room},
     *        посортированных в порядке возрастания времени.
     *        Если флаг {@param inclusive} установлен - ты получишь так же в запросе
     *        message {@param beforeId}.
     */
    public List<Message> getMessagesBefore(String room, int count,
                                           int beforeId, boolean inclusive)
    {
        return pool.select("SELECT * FROM " +
                        "(SELECT * FROM messages " +
                        "WHERE room = ? " +
                        "AND topic_id IS NULL " +
                        "AND id <" + (inclusive?"=":"") + " ? " +
                        "ORDER BY time DESC " +
                        "LIMIT ?) as result " +
                        "ORDER BY time ASC;",
                new Object[]{room, beforeId, count},
                Chat::parseMessages
        );
    }

    public Message getMessageById(int messageId) {
        return pool.select("SELECT * FROM messages WHERE id = ?",
                new Object[]{messageId},
                rs -> rs.next() ? new Message(rs) : null
        );
    }

    public Message saveMessage(Message message) {
        List<Object> objects = pool.batch(Arrays.asList("INSERT INTO messages " +
                        "(room, topic_id, player_id, time, text) " +
                        "VALUES (?, ?, ?, ?, ?);",
                pool.getLastInsertedIdQuery("messages", "id")),

                Arrays.asList(new Object[]{
                                message.getRoom(),
                                message.getTopicId(),
                                message.getPlayerId(),
                                JDBCTimeUtils.toString(new Date(message.getTime())),
                                message.getText()
                        },
                        new Object[]{}),

                Arrays.asList(null,
                        rs -> rs.next() ? rs.getInt(1) : null));


        message.setId((Integer) objects.get(1));
        return message;
    }

    public boolean deleteMessage(int id, String playerId) {
        return 1 == pool.update("DELETE FROM messages " +
                "WHERE id = ? " +
                "AND player_id = ?",
                new Object[]{id, playerId});
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
        private String room;
        private String playerId;
        private long time;
        private String text;

        @Builder
        public Message(String room, Integer topicId, String playerId, long time, String text) {
            this.room = room;
            this.topicId = topicId;
            this.playerId = playerId;
            this.time = time;
            this.text = text;
        }

        private Message(ResultSet rs) throws SQLException {
            id = rs.getInt("id");
            room = rs.getString("room");
            topicId = (Integer) rs.getObject("topic_id");
            playerId = rs.getString("player_id");
            time = JDBCTimeUtils.getTimeLong(rs);
            text = rs.getString("text");
        }
    }
}
