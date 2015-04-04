package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.jdbc.For;
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
        pool.run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                String sql = "INSERT INTO saves " +
                        "(time, name, callbackUrl, gameName, score) " +
                        "VALUES (?,?,?,?,?);";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setTime(1, new Time(System.currentTimeMillis()));
                    stmt.setString(2, player.getName());
                    stmt.setString(3, player.getCallbackUrl());
                    stmt.setString(4, player.getGameName());
                    stmt.setInt(5, player.getScore());
                    stmt.execute();

                } catch (SQLException e) {
                    throw new RuntimeException("Error saving scores for user: " + player.getName(), e);
                }
                return null;
            }
        });
    }

    @Override
    public PlayerSave loadGame(final String name) {
        return pool.run(new For<PlayerSave>() {
            @Override
            public PlayerSave run(Connection connection) {
                String sql = "SELECT * FROM saves WHERE name = ? ORDER BY time DESC LIMIT 1;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {

                        String callbackUrl = resultSet.getString("callbackUrl");
                        int score = resultSet.getInt("score");
                        String gameName = resultSet.getString("gameName");
                        String protocol = Protocol.WS.name();
                        return new PlayerSave(name, callbackUrl, gameName, score, protocol);
                    } else {
                        return PlayerSave.NULL;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error load game for user: " + name, e);
                }
            }
        });
    }

    @Override
    public List<String> getSavedList() {
        return pool.run(new For<List<String>>() {
            @Override
            public List<String> run(Connection connection) {
                String sql = "SELECT DISTINCT name FROM saves;"; // TODO убедиться, что загружены самые последние

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    ResultSet resultSet = stmt.executeQuery();
                    List<String> result = new LinkedList<String>();
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        result.add(name);
                    }
                    return result;
                } catch (SQLException e) {
                    throw new RuntimeException("Error get all saves", e);
                }
            }
        });
    }

    @Override
    public void delete(final String name) {
        pool.run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                String sql = "DELETE FROM saves WHERE name = ?;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, name);
                    stmt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException("Error get all saves", e);
                }
                return null;
            }
        });
    }

    @Override
    public void saveChat(final List<ChatMessage> messages) {
        pool.run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                String sql = "INSERT INTO chats " +
                        "(time, name, message) " +
                        "VALUES (?,?,?);";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    for (ChatMessage message : messages) {
                        stmt.setTime(1, new Time(message.getTime().getTime()));
                        stmt.setString(2, message.getPlayerName());
                        stmt.setString(3, message.getMessage());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                } catch (SQLException e) {
                    throw new RuntimeException("Error saving chat messages", e);
                }
                return null;
            }
        });
    }

    @Override
    public List<ChatMessage> loadChat() {
        return pool.run(new For<List<ChatMessage>>() {
            @Override
            public List<ChatMessage> run(Connection connection) {
                String sql = "SELECT * FROM chats ORDER BY time ASC;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    ResultSet resultSet = stmt.executeQuery();
                    List<ChatMessage> result = new LinkedList<ChatMessage>();
                    while (resultSet.next()) {
                        String name = resultSet.getString("name");
                        Time time = resultSet.getTime("time");
                        String message = resultSet.getString("message");
                        result.add(new ChatMessage(new Date(time.getTime()), name, message));
                    }
                    return result;
                } catch (SQLException e) {
                    throw new RuntimeException("Error get all saves", e);
                }
            }
        });
    }
}
