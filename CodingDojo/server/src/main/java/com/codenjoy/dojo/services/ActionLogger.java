package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.jdbc.For;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * User: sanja
 * Date: 11.01.14
 * Time: 2:26
 */
@Component("actionLogger")
public class ActionLogger {

    private final int ticksPerSave;

    private Queue<BoardLog> cache = new ConcurrentLinkedQueue<BoardLog>();
    private int count;
    private boolean active;

    private SqliteConnectionThreadPool pool;

    public ActionLogger(String dbFile, int ticksPerSave) {
        this.ticksPerSave = ticksPerSave;
        pool = new SqliteConnectionThreadPool(dbFile,
                "CREATE TABLE IF NOT EXISTS player_boards (" +
                    "time int, " +
                    "player_name varchar(255), " +
                    "game_type varchar(2000), " +
                    "score int, " +
                    "board varchar(255));");
        active = false;
        count = 0;
    }

    public void pause() {
        active = false;
    }

    public void resume() {
        active = true;
    }

    public boolean isRecording() {
        return active;
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public void saveToDB() {
        pool.run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                String sql = "INSERT INTO player_boards " +
                        "(time, player_name, game_type, score, board) " +
                        "VALUES (?,?,?,?,?);";

                BoardLog data = null;
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    int size = cache.size();
                    for (int i = 0; i < size; i++) {
                        data = cache.poll();
                        if (data == null) {
                            break;
                        }

                        stmt.setTime(1, new Time(data.getTime()));
                        stmt.setString(2, data.getPlayerName());
                        stmt.setString(3, data.getGameType());
                        stmt.setInt(4, data.getScore());
                        stmt.setString(5, data.getBoard());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                } catch (SQLException e) {
                    throw new RuntimeException("Error saving log", e);
                }
                return null;
            }
        });
    }

    public void log(PlayerGames playerGames) {
        if (!active) return;

        long tick = System.currentTimeMillis();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            cache.add(new BoardLog(tick,
                    player.getName(),
                    player.getGameName(),
                    player.getScore(),
                    playerGame.getGame().getBoardAsString()));
        }

        if (count++ % ticksPerSave == 0) {
            saveToDB();
        }
    }

    public List<BoardLog> getAll() {
        return pool.select("SELECT * FROM player_boards;",
                new ObjectMapper<List<BoardLog>>() {
                    @Override
                    public List<BoardLog> mapFor(ResultSet resultSet) throws SQLException {
                        List<BoardLog> result = new LinkedList<BoardLog>();
                        while (resultSet.next()) {
                            result.add(new BoardLog(resultSet));
                        }
                        return result;
                    }
                }
        );
    }
}
