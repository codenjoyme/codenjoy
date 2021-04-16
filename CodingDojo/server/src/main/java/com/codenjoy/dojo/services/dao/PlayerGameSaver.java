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
import com.codenjoy.dojo.services.jdbc.*;
import org.json.JSONObject;

import java.sql.Date;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class PlayerGameSaver implements GameSaver {

    public static final String INSERT_SAVES_QUERY = "INSERT INTO saves " +
                    "(time, player_id, callback_url, room_name, game_name, score, save) " +
                    "VALUES (?,?,?,?,?,?,?);";

    public static final String INSERT_SAVES_PLAYERS_QUERY = "INSERT INTO saves_players " +
                    "(player_id, room_name) " +
                    "VALUES (?,?) " +
                    "EXCEPT " +
                    "SELECT player_id, room_name " +
                    "FROM saves_players " +
                    "WHERE player_id = ? " +
                    "AND room_name = ?;";

    private CrudConnectionThreadPool pool;

    public PlayerGameSaver(ConnectionThreadPoolFactory factory) {
        // TODO renname room_name -> room, game_name -> game
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS saves (" +
                        "time varchar(255), " +
                        "player_id varchar(255), " +
                        "callback_url varchar(255)," +
                        "room_name varchar(255)," +
                        "game_name varchar(255)," +
                        "score varchar(255)," +
                        "save varchar(255));",
                // TODO вторая табличка костыль для производительности потому как DISTINCT на большой таблице очень долго ранается, вообще это давно пора переделать
                "CREATE TABLE IF NOT EXISTS saves_players (" +
                        "player_id varchar(255), " +
                        "room_name varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    private static class Save {

        private Game game;
        private Player player;
        private String time;

        public Save(PlayerGame playerGame, String time) {
            this.player = playerGame.getPlayer();
            this.game = playerGame.getGame();
            this.time = time;
        }

        public String getSave() {
            return game.getSave().toString();
        }

        public String getScore() {
            return player.getScore().toString();
        }

        public String getGame() {
            return player.getGame();
        }

        public String getRoom() {
            return player.getRoom();
        }

        public String getCallbackUrl() {
            return player.getCallbackUrl();
        }

        public String getId() {
            return player.getId();
        }

        public String getTime() {
            return time;
        }

        @Override
        public String toString() {
            return String.format(
                    "Save[time:%s, id:%s, url:%s, game:%s, " +
                    "room:%s, score:%s, save:%s]",
                    getTime(),
                    getId(),
                    getCallbackUrl(),
                    getGame(),
                    getRoom(),
                    getScore(),
                    getSave());
        }
    }

    @Override
    public void saveGames(List<PlayerGame> playerGames, long time) {
        String timeString = JDBCTimeUtils.toString(new Date(time));
        List<Save> data = playerGames.stream()
                .map(playerGame -> new Save(playerGame, timeString))
                .collect(toList());

        // TODO надо тут еще транзакцию навешать на эти два запроса
        pool.batchUpdate(INSERT_SAVES_QUERY,
                data,
                (stmt, save) -> {
                    stmt.setObject(1, save.getTime());
                    stmt.setObject(2, save.getId());
                    stmt.setObject(3, save.getCallbackUrl());
                    stmt.setObject(4, save.getRoom());
                    stmt.setObject(5, save.getGame());
                    stmt.setObject(6, save.getScore());
                    stmt.setObject(7, save.getSave());
                    return true;
                });

        pool.batchUpdate(INSERT_SAVES_PLAYERS_QUERY,
                data,
                (stmt, save) -> {
                    stmt.setObject(1, save.getId());
                    stmt.setObject(2, save.getRoom());
                    stmt.setObject(3, save.getId());
                    stmt.setObject(4, save.getRoom());

                    return true;
                });
    }

    @Override
    public void saveGame(Player player, String save, long time) {
        // TODO надо тут еще транзакцию навешать на эти два запроса
        pool.update(INSERT_SAVES_QUERY,
                new Object[]{
                        JDBCTimeUtils.toString(new Date(time)),
                        player.getId(),
                        player.getCallbackUrl(),
                        player.getRoom(),
                        player.getGame(),
                        player.getScore(),
                        save
                });

        pool.update(INSERT_SAVES_PLAYERS_QUERY,
                new Object[]{
                        player.getId(),
                        player.getRoom(),
                        player.getId(),
                        player.getRoom()
                });
    }

    @Override
    public PlayerSave loadGame(String id) {
        return pool.select("SELECT * " +
                        "FROM saves " +
                        "WHERE player_id = ? " +
                        "ORDER BY time DESC " +
                        "LIMIT 1;",
                new Object[]{id},
                rs -> {
                    if (rs.next()) {
                        String callbackUrl = rs.getString("callback_url");
                        String score = rs.getString("score");
                        String room = rs.getString("room_name");
                        String game = rs.getString("game_name");
                        String save = rs.getString("save");
                        return new PlayerSave(id, callbackUrl, game, room, score, save);
                    } else {
                        return PlayerSave.NULL;
                    }
                }
        );
    }

    @Override
    public List<String> getSavedList() {
        // TODO убедиться, что загружены самые последние
        return pool.select("SELECT DISTINCT player_id " +
                        "FROM saves_players;",
                rs -> new LinkedList<String>(){{
                    while (rs.next()) {
                        add(rs.getString("player_id"));
                    }
                }});
    }

    @Override
    public List<String> getSavedList(String room) {
        // TODO убедиться, что загружены самые последние
        return pool.select("SELECT DISTINCT player_id " +
                        "FROM saves_players " +
                        "WHERE room_name = ?;",
                new Object[]{room},
                rs -> new LinkedList<String>(){{
                    while (rs.next()) {
                        add(rs.getString("player_id"));
                    }
                }});
    }

    @Override
    public void delete(String id) {
        pool.update("DELETE FROM saves " +
                        "WHERE player_id = ?;",
                new Object[]{id});

        pool.update("DELETE FROM saves_players " +
                        "WHERE player_id = ?;",
                new Object[]{id});
    }

    @Override
    public void delete(String id, String room) {
        pool.update("DELETE FROM saves " +
                        "WHERE player_id = ? " +
                        "AND room_name = ?;",
                new Object[]{id, room});

        pool.update("DELETE FROM saves_players " +
                        "WHERE player_id = ? " +
                        "AND room_name = ?;",
                new Object[]{id, room});
    }
}
