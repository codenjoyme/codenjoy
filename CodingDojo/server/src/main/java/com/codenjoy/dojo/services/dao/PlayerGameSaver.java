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


import com.codenjoy.dojo.services.GameSaver;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerSave;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PlayerGameSaver implements GameSaver {

    private CrudConnectionThreadPool pool;

    public PlayerGameSaver(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS saves (" +
                        "time varchar(255), " +
                        "player_id varchar(255), " +
                        "callback_url varchar(255)," +
                        "room_name varchar(255)," +
                        "game_name varchar(255)," +
                        "score varchar(255)," +
                        "save varchar(255)," +
                        "repository_url varchar(255)," +
                        "subscribed BOOLEAN);");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    @Override
    public void saveGame(Player player, String save, long time) {
        pool.update("INSERT INTO saves " +
                        "(time, player_id, callback_url, room_name, game_name, score, save, repository_url) " +
                        "VALUES (?,?,?,?,?,?,?,?);",
                new Object[]{JDBCTimeUtils.toString(new Date(time)),
                        player.getId(),
                        player.getCallbackUrl(),
                        player.getRoom(),
                        player.getGame(),
                        player.getScore(),
                        save,
                        player.getRepositoryUrl()
        });
    }

    @Override
    public void updateGame(Player player, String save, long time) {
        pool.update("UPDATE saves " +
                        "SET time = ?, callback_url = ?, room_name = ?, game_name = ?, score = ?, save = ?, repository_url = ?" +
                        "WHERE player_id = ?;",
                new Object[]{JDBCTimeUtils.toString(new Date(time)),
                        player.getCallbackUrl(),
                        player.getRoom(),
                        player.getGame(),
                        player.getScore(),
                        save,
                        player.getRepositoryUrl(),
                        player.getId()
                });
    }

    @Override
    public void updateGame(PlayerSave playerSave, String save, long time) {
        pool.update("UPDATE saves " +
                        "SET time = ?, callback_url = ?, room_name = ?, game_name = ?, score = ?, save = ?, repository_url = ?" +
                        "WHERE player_id = ?;",
                new Object[]{JDBCTimeUtils.toString(new Date(time)),
                        playerSave.getCallbackUrl(),
                        playerSave.getRoom(),
                        playerSave.getGame(),
                        playerSave.getScore(),
                        save,
                        playerSave.getRepositoryUrl(),
                        playerSave.getId()
                });
    }


    @Override
    public PlayerSave loadGame(String id) {
        return pool.select("SELECT * FROM saves WHERE player_id = ? ORDER BY time DESC LIMIT 1;",
                new Object[]{id},
                rs -> {
                    if (rs.next()) {
                        String callbackUrl = rs.getString("callback_url");
                        String score = rs.getString("score");
                        String room = rs.getString("room_name");
                        String game = rs.getString("game_name");
                        String save = rs.getString("save");
                        String repositoryUrl = rs.getString("repository_url");
                        return new PlayerSave(id, callbackUrl, game, room, score, save, repositoryUrl);
                    } else {
                        return PlayerSave.NULL;
                    }
                }
        );
    }

    @Override
    public List<String> getSavedList() {
        return pool.select("SELECT DISTINCT player_id FROM saves;", // TODO убедиться, что загружены самые последние
                rs -> {
                    List<String> result = new LinkedList<>();
                    while (rs.next()) {
                        String id = rs.getString("player_id");
                        result.add(id);
                    }
                    return result;
                }
        );
    }

    @Override
    public void delete(String id) {
        pool.update("DELETE FROM saves WHERE player_id = ?;",
                new Object[]{id});
    }

    @Override
    public void updateScore(String playerId, String gameName, long score, long time) {
        pool.update("UPDATE saves " +
                        "SET time = ?, score = ?" +
                        "WHERE player_id = ? AND game_name = ?;",
                new Object[]{JDBCTimeUtils.toString(new Date(time)),
                        score,
                        playerId,
                        gameName
                });
    }

    @Override
    public List<PlayerSave> loadAllSaves() {
        return pool.select("SELECT * FROM saves",
                rs -> {
                    List<PlayerSave> result = new LinkedList<>();
                    while (rs.next()) {
                        Player player = new Player(rs.getString("player_id"));
                        player.setCallbackUrl(rs.getString("callback_url"));
                        player.setScore(rs.getString("score"));
                        player.setGame(rs.getString("game_name"));
                        player.setRoom(rs.getString("room_name"));
                        player.setData(rs.getString("save"));
                        player.setRepositoryUrl(rs.getString("repository_url"));
                        result.add(new PlayerSave(player));
                    }
                    return result;
                });

    }

    @Override
    public Map<String, String> getEventsList() {
        return pool.select("SELECT DISTINCT room_name, game_name FROM saves;",
                rs -> {
                    Map<String, String> result = new HashMap<>();
                    while (rs.next()) {
                        String roomName = rs.getString("room_name");
                        String gameName = rs.getString("game_name");
                        result.put(roomName, gameName);
                    }
                    return result;
                }
        );
    }

    @Override
    public List<String> getUserGames(String id) {
        return pool.select("SELECT DISTINCT game_name FROM saves WHERE player_id = ?;",
                new Object[]{id},
                rs -> {
                    List<String> result = new ArrayList<>();
                    while (rs.next()) {
                        String gameName = rs.getString("game_name");
                        result.add(gameName);
                    }
                    return result;
                }
        );
    }

    @Override
    public String getRoomNameByPlayerId(String id) {
        return pool.select("SELECT room_name FROM saves " +
                        "WHERE player_id = ?",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("room_name") : null
        );
    }

    public String getRepositoryByPlayerIdForGame(String id, String game) {
        return pool.select("SELECT repository_url FROM saves " +
                        "WHERE player_id = ? AND game_name = ?",
                new Object[]{id,
                        game
                },
                rs -> rs.next() ? rs.getString("repository_url") : null
        );
    }
}
