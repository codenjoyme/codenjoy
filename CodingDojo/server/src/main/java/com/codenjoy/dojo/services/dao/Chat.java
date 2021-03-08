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

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final CrudConnectionThreadPool pool;

    public Chat(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS messages (" +
                        // TODO this works only for sqlite
                        "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "chat_id varchar(255), " +
                        "player_id varchar(255), " +
                        "time varchar(255), " +
                        "text varchar(255));"
        );
    }

    public void removeDatabase() {
        pool.removeDatabase();
    }

    public List<Message> getMessages(String chatId, int count) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE chat_id = ? " +
                        "ORDER BY time " +
                        "LIMIT ?;",
                new Object[]{chatId, count},
                Chat::parseMessages
        );
    }

    public List<Message> getMessagesBetween(String chatId, int count, int afterId, int beforeId) {
        if (afterId > beforeId) {
            throw new IllegalArgumentException("afterId in interval should be smaller than beforeId");
        }
        return pool.select("SELECT * FROM messages " +
                        "WHERE chat_id = ? AND id > ? AND id < ?" +
                        "ORDER BY time " +
                        "LIMIT ?;",
                new Object[]{chatId, afterId, beforeId, count},
                Chat::parseMessages
        );
    }

    public List<Message> getMessagesAfter(String chatId, int count, int afterId) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE chat_id = ? AND id > ?" +
                        "ORDER BY time " +
                        "LIMIT ?;",
                new Object[]{chatId, afterId, count},
                Chat::parseMessages
        );
    }

    public List<Message> getMessagesBefore(String chatId, int count, int beforeId) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE chat_id = ? AND id < ?" +
                        "ORDER BY time " +
                        "LIMIT ?;",
                new Object[]{chatId, beforeId, count},
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
        pool.update("INSERT INTO messages " +
                        "(id, chat_id, player_id, time, text) " +
                        "VALUES (?, ?, ?, ?, ?);",
                new Object[]{
                        null,
                        message.getChatId(),
                        message.getPlayerId(),
                        JDBCTimeUtils.toString(new Date(message.getTime())),
                        message.getText()
                }
        );
        message.setId(pool.lastInsertId("messages", "id"));
        return message;
    }

    public void deleteMessage(int id) {
        pool.update("DELETE FROM messages WHERE id = ?", new Object[]{id});
    }

    private static List<Message> parseMessages(ResultSet rs) throws SQLException {
        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            messages.add(new Message(rs));
        }
        return messages;
    }

    public void removeAll() {
        // TODO this works only for sqlite
        pool.update("UPDATE sqlite_sequence SET seq = 0 WHERE name = 'messages'");
        pool.update("DELETE FROM messages");
    }

    @Data
    public static class Message {
        private Integer id;
        private String chatId;
        private String playerId;
        private long time;
        private String text;

        @Builder
        public Message(String chatId, String playerId, long time, String text) {
            this.chatId = chatId;
            this.playerId = playerId;
            this.time = time;
            this.text = text;
        }

        private Message(ResultSet rs) throws SQLException {
            this.id = rs.getInt("id");
            this.chatId = rs.getString("chat_id");
            this.playerId = rs.getString("player_id");
            this.time = JDBCTimeUtils.getTimeLong(rs);
            this.text = rs.getString("text");
        }
    }
}
