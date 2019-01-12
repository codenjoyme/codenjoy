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


import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class Players {

    private CrudConnectionThreadPool pool;

    public Players(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS players (" +
                        "email varchar(255), " +
                        "first_name varchar(255), " +
                        "last_name varchar(255), " +
                        "password varchar(255)," +
                        "city varchar(255)," +
                        "skills varchar(255)," +
                        "comment varchar(255)," +
                        "code varchar(255)," +
                        "server varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public List<Player> getPlayersDetails() {
        return pool.select("SELECT * FROM players;",
                rs -> {
                    List<Player> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(
                                new Player(rs.getString("email"),
                                rs.getString("firstName"),
                                rs.getString("lastName"), 
                                rs.getString("password"),
                                rs.getString("city"),
                                rs.getString("skills"),
                                rs.getString("comment"), 
                                rs.getString("code"),
                                rs.getString("server")));
                    }
                    return result;
                }
        );
    }

    public List<ServerLocation> getPlayersLocations() {
        return pool.select("SELECT * FROM players;",
                rs -> {
                    List<ServerLocation> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(
                            new ServerLocation(
                                rs.getString("email"),
                                rs.getString("code"),
                                rs.getString("server")));
                    }
                    return result;
                }
        );
    }

    public String getCode(String email) {
        return pool.select("SELECT code FROM players WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? rs.getString("code") : null
        );
    }


    public String getServer(String email) {
        return pool.select("SELECT server FROM players WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? rs.getString("server") : null
        );
    }

    public void create(Player player) {
        Object[] parameters = {player.getPassword(), player.getCode(), player.getServer(), player.getEmail()};
        pool.update("INSERT INTO players (password, code, server, email) VALUES (?,?,?,?);",
                parameters);
    }

    public void update(Player player) {
        Object[] parameters = {player.getPassword(), player.getCode(), player.getServer(), player.getEmail()};
        pool.update("UPDATE players SET password = ?, code = ?, server = ? WHERE email = ?;",
                parameters);
    }

    public void remove(String email) {
        pool.update("DELETE FROM players WHERE email = ?;",
                new Object[]{email});
    }

}
