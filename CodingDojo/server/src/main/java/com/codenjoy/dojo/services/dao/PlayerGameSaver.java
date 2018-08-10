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
import com.codenjoy.dojo.services.jdbc.*;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PlayerGameSaver implements GameSaver {

    private CrudConnectionThreadPool pool;

    public PlayerGameSaver(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS saves (" +
                        "time varchar(255), " +
                        "name varchar(255), " +
                        "callbackUrl varchar(255)," +
                        "gameName varchar(255)," +
                        "score int," +
                        "save varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    @Override
    public void saveGame(final Player player, final String save) {
        pool.update("INSERT INTO saves " +
                        "(time, name, callbackUrl, gameName, score, save) " +
                        "VALUES (?,?,?,?,?,?);",
                new Object[]{JDBCTimeUtils.toString(new Date(System.currentTimeMillis())),
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
                            return new PlayerSave(name, callbackUrl, gameName, score, save);
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
}
