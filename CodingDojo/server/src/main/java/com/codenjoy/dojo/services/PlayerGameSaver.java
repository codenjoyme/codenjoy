package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.jdbc.For;
import com.codenjoy.dojo.services.jdbc.ForStmt;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/22/13
 * Time: 10:55 PM
 */
@Component("gameSaver")
public class PlayerGameSaver implements GameSaver {

    private SqliteConnectionThreadPool pool;

    public PlayerGameSaver(String dbFile) {
        pool = new SqliteConnectionThreadPool(dbFile,
                "CREATE TABLE IF NOT EXISTS saves (" +
                        "time int, " +
                        "name varchar(255), " +
                        "callbackUrl varchar(255)," +
                        "gameName varchar(255)," +
                        "score int);",
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
    public void saveGame(final Player player) {
        pool.update("INSERT INTO saves " +
                        "(time, name, callbackUrl, gameName, score) " +
                        "VALUES (?,?,?,?,?);",
                new Object[]{new Time(System.currentTimeMillis()),
                        player.getName(),
                        player.getCallbackUrl(),
                        player.getGameName(),
                        player.getScore()
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
                            String protocol = Protocol.WS.name();
                            return new PlayerSave(name, callbackUrl, gameName, score, protocol);
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
