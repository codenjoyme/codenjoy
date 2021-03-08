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
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import lombok.Builder;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {
    private final CrudConnectionThreadPool pool;

    public ChatDAO(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS messages (" +
                        "id varchar(255), " +
                        "room_id varchar(255), " +
                        "player_id varchar(255), " +
                        "timestamp varchar(255), " +
                        "text varchar(255));"
        );
    }

    public void removeDatabase() {
        pool.removeDatabase();
    }

    public List<Message> getMessages(String roomId, int count) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? " +
                        "ORDER BY timestamp " +
                        "LIMIT " + count + ";",
                new Object[]{roomId},
                ChatDAO::parseMessages
        );
    }

    public List<Message> getMessagesBetweenIds(String roomId, int count, int beforeId, int afterId) {
        if (afterId <= beforeId) {
            throw new IllegalArgumentException("First id in interval should be smaller than second");
        }
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? AND id > ? AND id < ?" +
                        "ORDER BY timestamp " +
                        "LIMIT " + count + ";",
                new Object[]{roomId, afterId, beforeId},
                ChatDAO::parseMessages
        );
    }

    public List<Message> getMessagesAfterId(String roomId, int count, int afterId) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? AND id > ?" +
                        "ORDER BY timestamp " +
                        "LIMIT " + count + ";",
                new Object[]{roomId, afterId},
                ChatDAO::parseMessages
        );
    }

    public List<Message> getMessagesBeforeId(String roomId, int count, int beforeId) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? AND id < ?" +
                        "ORDER BY timestamp " +
                        "LIMIT " + count + ";",
                new Object[]{roomId, beforeId},
                ChatDAO::parseMessages
        );
    }

    public Message getMessageById(int messageId) {
        return pool.select("SELECT * FROM messages WHERE id = ?",
                new Object[]{messageId},
                Message::new
        );
    }

    // TODO id creation probably cause race condition, find another solution
    @SuppressWarnings("UnusedReturnValue")
    public Message saveMessage(Message message) {
        message.setId(generateId());
        pool.update("INSERT INTO messages " +
                        "(id, room_id, player_id, timestamp, text) " +
                        "VALUES (?, ?, ?, ?, ?);",
                new Object[]{
                        message.getId(),
                        message.getRoomId(),
                        message.getPlayerId(),
                        JDBCTimeUtils.toString(message.getTimestamp()),
                        message.getText()
                }
        );
        return message;
    }

    public boolean deleteMessage(int id, String roomId, String playerId) {
        try {
            pool.update(
                    "DELETE FROM messages WHERE id = ? AND room_id = ? AND player_id = ?",
                    new Object[]{id, roomId, playerId}
            );
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private static List<Message> parseMessages(ResultSet rs) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            messages.add(new Message(rs));
        }
        return messages;
    }

    private int generateId() {
        Message lastMessage = pool.select(
                "SELECT * FROM messages ORDER BY id DESC LIMIT 1;",
                rs -> rs.next() ? new Message(rs) : null
        );
        return lastMessage == null ? 0 : lastMessage.id + 1;
    }

    @Data
    public static class Message {
        private Integer id;
        private String roomId;
        private String playerId;
        private LocalDateTime timestamp;
        private String text;

        @Builder
        private Message(String roomId, String playerId, LocalDateTime timestamp, String text) {
            this.roomId = roomId;
            this.playerId = playerId;
            this.timestamp = timestamp;
            this.text = text;
        }

        private Message(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.roomId = rs.getString("room_id");
            this.playerId = rs.getString("player_id");
            this.timestamp = JDBCTimeUtils.toLocalDateTime(rs.getString("timestamp"));
            this.text = rs.getString("text");
        }
    }
}
