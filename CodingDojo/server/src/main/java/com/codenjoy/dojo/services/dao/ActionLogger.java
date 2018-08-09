package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.BoardLog;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.jdbc.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ActionLogger {

    private final int ticksPerSave;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Queue<BoardLog> cache = new ConcurrentLinkedQueue<BoardLog>();
    private int count;
    private boolean active;

    private CrudConnectionThreadPool pool;

    public ActionLogger(ConnectionThreadPoolFactory factory, int ticksPerSave) {
        this.ticksPerSave = ticksPerSave;
        pool = factory.create("CREATE TABLE IF NOT EXISTS player_boards (" +
                    "time varchar(255), " +
                    "player_name varchar(255), " +
                    "game_type varchar(255), " +
                    "score varchar(255), " +
                    "board varchar(10000));");
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

                        stmt.setString(1, JDBCTimeUtils.toString(new Date(data.getTime())));
                        stmt.setString(2, data.getPlayerName());
                        stmt.setString(3, data.getGameType());
                        stmt.setString(4, data.getScore().toString());
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
        if (!active || playerGames.size() == 0) return;

        long tick = System.currentTimeMillis();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            cache.add(new BoardLog(tick,
                    player.getName(),
                    player.getGameName(),
                    player.getScore(),
                    playerGame.getGame().getBoardAsString().toString()));
        }

        if (count++ % ticksPerSave == 0) {
            // executor.submit потому что sqlite тормозит при сохранении
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    saveToDB();
                }
            });
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
