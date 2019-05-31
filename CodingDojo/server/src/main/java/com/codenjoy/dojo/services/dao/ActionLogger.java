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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActionLogger extends Suspendable {

    @Value("${board.save.ticks}")
    private int ticks;

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Queue<BoardLog> cache = new ConcurrentLinkedQueue<>();
    private int count;

    private CrudConnectionThreadPool pool;

    public ActionLogger(ConnectionThreadPoolFactory factory) {
        pool = factory.create("CREATE TABLE IF NOT EXISTS player_boards (" +
                    "time varchar(255), " +
                    "player_name varchar(255), " +
                    "game_type varchar(255), " +
                    "score varchar(255), " +
                    "command varchar(255), " +
                    "board varchar(10000));");
        active = false;
        count = 0;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public void saveToDB() {
        pool.run(connection -> {
            String sql = "INSERT INTO player_boards " +
                    "(time, player_name, game_type, score, command, board) " +
                    "VALUES (?,?,?,?,?,?);";

            BoardLog data;
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
                    stmt.setString(5, data.getCommand());
                    stmt.setString(6, data.getBoard());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException("Error saving log", e);
            }
            return null;
        });
    }

    public void log(PlayerGames playerGames) {
        if (!active || playerGames.size() == 0) return;

        long tick = now();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            cache.add(new BoardLog(tick,
                    player.getName(),
                    player.getGameName(),
                    player.getScore(),
                    playerGame.getGame().getBoardAsString().toString(),
                    playerGame.popLastCommand()));
        }

        if (count++ % ticks == 0) {
            // executor.submit потому что sqlite тормозит при сохранении
            executor.submit(() -> saveToDB());
        }
    }

    protected long now() {
        return System.currentTimeMillis();
    }

    public List<BoardLog> getAll() {
        return pool.select("SELECT * FROM player_boards;",
                rs -> getBoardLogs(rs));
    }

    private LinkedList<BoardLog> getBoardLogs(ResultSet rs) throws SQLException {
        return new LinkedList<BoardLog>(){{
                while (rs.next()) {
                    add(new BoardLog(rs));
                }
            }};
    }

    // TODO test me
    public long getLastTime(String player) {
        return pool.select("SELECT MAX(time) AS time FROM player_boards WHERE player_name = ?;",
                new Object[]{ player },
                rs -> (rs.next()) ? JDBCTimeUtils.getTimeLong(rs) : 0);
    }

    // TODO test me
    public List<BoardLog> getBoardLogsFor(String player, long time, int count) {
        return pool.select(
                    "SELECT * FROM (SELECT * FROM player_boards WHERE player_name = ? AND time <= ? ORDER BY time ASC LIMIT ?) AS before" +
                    " UNION " +
                    "SELECT * FROM (SELECT * FROM player_boards WHERE player_name = ? AND time > ? ORDER BY time ASC LIMIT ?) AS after;",
                new Object[]{
                    player, JDBCTimeUtils.toString(new java.util.Date(time)), count + 1,
                    player, JDBCTimeUtils.toString(new java.util.Date(time)), count
                },
                rs -> getBoardLogs(rs));
    }
}
