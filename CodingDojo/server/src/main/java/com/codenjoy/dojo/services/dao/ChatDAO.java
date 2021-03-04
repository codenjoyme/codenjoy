package com.codenjoy.dojo.services.dao;

import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import com.codenjoy.dojo.web.rest.pojo.PMessage;

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

    public PMessage saveMessage(PMessage message) {
        PMessage lastMessage = getLastMessage(message.getRoomId());
        int id = lastMessage != null
                ? Integer.parseInt(lastMessage.getId()) + 1
                : 0;

        message.setId(Integer.toString(id));
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

    public List<PMessage> getMessages(String roomId, LocalDateTime from, int count) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? AND timestamp >= ? " +
                        "ORDER BY timestamp DESC " +
                        "LIMIT " + (count + 1) + ";",
                new Object[]{
                        roomId,
                        JDBCTimeUtils.toString(from)
                },
                ChatDAO::parseMessages
        );
    }

    public PMessage getLastMessage(String roomId) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? " +
                        "ORDER BY timestamp DESC " +
                        "LIMIT 1;",
                new Object[]{roomId},
                rs -> rs.next() ? retrieveMessage(rs) : null
        );
    }

    public PMessage getMessageBefore(String roomId, LocalDateTime timestamp) {
        return pool.select("SELECT * FROM messages " +
                        "WHERE room_id = ? AND timestamp < ? " +
                        "ORDER BY timestamp DESC LIMIT 1;",
                new Object[]{
                        roomId,
                        JDBCTimeUtils.toString(timestamp)
                },
                rs -> rs.next() ? retrieveMessage(rs) : null
        );
    }

    private static PMessage retrieveMessage(ResultSet rs) throws SQLException {
        return PMessage.builder()
                .playerId(rs.getString("player_id"))
                .timestamp(JDBCTimeUtils.toLocalDateTime(rs.getString("timestamp")))
                .text(rs.getString("text"))
                .roomId(rs.getString("room_id"))
                .id(rs.getString("id"))
                .build();
    }

    public boolean deleteMessage(String id) {
        try {
            pool.update("DELETE FROM messages WHERE id = ?", new Object[]{id});
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private static List<PMessage> parseMessages(ResultSet rs) throws SQLException {
        List<PMessage> messages = new ArrayList<>();
        while (rs.next()) {
            messages.add(retrieveMessage(rs));
        }
        return messages;
    }
}
