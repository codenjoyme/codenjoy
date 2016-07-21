package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.GameSaver;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerSave;
import com.codenjoy.dojo.services.Protocol;
import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.jdbc.ForStmt;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

@Component
public class PlayerGameSaver implements GameSaver {

    private SqliteConnectionThreadPool pool;

    public PlayerGameSaver(String dbFile) {
        pool = new SqliteConnectionThreadPool(dbFile,
                "CREATE TABLE IF NOT EXISTS saves (" +
                        "time int, " +
                        "name varchar(255), " +
                        "callbackUrl varchar(255)," +
                        "gameName varchar(255)," +
                        "score int," +
                        "save varchar(255));",
                "CREATE TABLE IF NOT EXISTS chats (" +
                        "time int, " +
                        "name varchar(255), " +
//                            "gameType varchar(255), " + // TODO сделать чтобы чат был в каждой комнате отдельный
                        "message varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    @Override
    public void saveGame(final Player player, final String save) {
        pool.update("INSERT INTO saves " +
                        "(time, name, callbackUrl, gameName, score, save) " +
                        "VALUES (?,?,?,?,?,?);",
                new Object[]{new Time(System.currentTimeMillis()),
                        player.getName(),
                        player.getCallbackUrl(),
                        player.getGameName(),
                        player.getScore(),
                        save
                });
    }

    @Override
    public PlayerSave loadGame(final String name) {
        return pool.select("SELECT * FROM saves WHERE name = ? ORDER BY time DESC LIMIT 1;",
                new Object[]{name},
                new ObjectMapper<PlayerSave>() {
                    @Override
                    public PlayerSave mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            String callbackUrl = resultSet.getString("callbackUrl");
                            int score = resultSet.getInt("score");
                            String gameName = resultSet.getString("gameName");
                            String save = resultSet.getString("save");
                            String protocol = Protocol.WS.name();
                            return new PlayerSave(name, callbackUrl, gameName, score, protocol, save);
                        } else {
                            return PlayerSave.NULL;
                        }
                    }
                }
        );
    }

    @Override
    public List<String> getSavedList() {
        return pool.select("SELECT DISTINCT name FROM saves;", // TODO убедиться, что загружены самые последние
                new ObjectMapper<List<String>>() {
                    @Override
                    public List<String> mapFor(ResultSet resultSet) throws SQLException {
                        List<String> result = new LinkedList<String>();
                        while (resultSet.next()) {
                            String name = resultSet.getString("name");
                            result.add(name);
                        }
                        return result;
                    }
                }
        );
    }

    @Override
    public void delete(final String name) {
        pool.update("DELETE FROM saves WHERE name = ?;",
                new Object[]{name});
    }

    @Override
    public void saveChat(final List<ChatMessage> messages) {
        final long last = getTimeLastChatMessage();

        pool.batchUpdate("INSERT INTO chats " +
                        "(time, name, message) " +
                        "VALUES (?,?,?);",
                messages,
                new ForStmt<ChatMessage>() {
                    @Override
                    public boolean run(PreparedStatement stmt, ChatMessage message) throws SQLException {
                        if (message.getTime().getTime() <= last) {
                            return false;
                        }
                        stmt.setTime(1, new Time(message.getTime().getTime()));
                        stmt.setString(2, message.getPlayerName());
                        stmt.setString(3, message.getMessage());
                        return true;
                    }
                });
    }

    private Long getTimeLastChatMessage() {
        return pool.select("SELECT * FROM chats ORDER BY time DESC LIMIT 1 OFFSET 0;",
                new ObjectMapper<Long>() {
                    @Override
                    public Long mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getTime("time").getTime();
                        }
                        return 0L;
                    }
                });
    }

    @Override
    public List<ChatMessage> loadChat() {
        return pool.select("SELECT * FROM chats ORDER BY time ASC;",
                new ObjectMapper<List<ChatMessage>>() {
                    @Override
                    public List<ChatMessage> mapFor(ResultSet resultSet) throws SQLException {
                        List<ChatMessage> result = new LinkedList<ChatMessage>();
                        while (resultSet.next()) {
                            String name = resultSet.getString("name");
                            Time time = resultSet.getTime("time");
                            String message = resultSet.getString("message");
                            result.add(new ChatMessage(new Date(time.getTime()), name, message));
                        }
                        return result;
                    }
                }
        );
    }
}
