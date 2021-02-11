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


import com.codenjoy.dojo.services.VerificationType;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toMap;

public class Players {

    private CrudConnectionThreadPool pool;

    public Players(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS players (" +
                        "id varchar(255), " +
                        "email varchar(255), " +
                        "phone varchar(255), " +
                        "first_name varchar(255), " +
                        "last_name varchar(255), " +
                        "password varchar(255)," +
                        "city varchar(255)," +
                        "skills varchar(255)," +
                        "comment varchar(255)," +
                        "code varchar(255)," +
                        "server varchar(255)," +
                        "approved int," +
                        "callback varchar(255)," +
                        "verification_code varchar(255)," +
                        "verification_type varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public List<Player> getPlayersDetails() {
        return selectPlayers("SELECT * FROM players;");
    }

    private List<Player> selectPlayers(String query) {
        return pool.select(query,
                rs -> {
                    List<Player> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(getPlayer(rs));
                    }
                    return result;
                }
        );
    }

    private Player getPlayer(ResultSet rs) throws SQLException {
        return new Player(
                rs.getString("id"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("password"),
                rs.getString("city"),
                rs.getString("skills"),
                rs.getString("comment"),
                rs.getString("code"),
                rs.getString("server"),
                rs.getInt("approved"),
                rs.getString("callback"),
                rs.getString("verification_code"),
                rs.getString("verification_type")
        );
    }

    public List<Player> getPlayers(List<String> ids) {
        return selectPlayers(String.format(
                "SELECT * FROM players WHERE id IN ('%s');",
                String.join("','", ids)
        ));
    }

    public List<ServerLocation> getPlayersLocations() {
        return pool.select("SELECT * FROM players;",
                rs -> {
                    List<ServerLocation> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(
                            new ServerLocation(
                                rs.getString("email"),
                                rs.getString("phone"),
                                rs.getString("id"),
                                rs.getString("code"),
                                rs.getString("server")));
                    }
                    return result;
                }
        );
    }

    public Player get(String id) {
        return pool.select("SELECT * FROM players WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? getPlayer(rs) : null
        );
    }

    public Player getByEmail(String email) {
        return pool.select("SELECT * FROM players WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? getPlayer(rs) : null
        );
    }

    public Player getByPhone(String phone) {
        return pool.select("SELECT * FROM players WHERE phone = ?;",
                new Object[]{phone},
                rs -> rs.next() ? getPlayer(rs) : null
        );
    }

    public String getCode(String email) {
        return pool.select("SELECT code FROM players WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? rs.getString("code") : null
        );
    }

    public String getServerByEmail(String email) {
        return pool.select("SELECT server FROM players WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? rs.getString("server") : null
        );
    }

    public String getServerByPhone(String phone) {
        return pool.select("SELECT server FROM players WHERE phone = ?;",
                new Object[]{phone},
                rs -> rs.next() ? rs.getString("server") : null
        );
    }

    public void create(Player player) {
        pool.update("INSERT INTO players (" +
                            "id, " +
                            "first_name, " +
                            "last_name, " +
                            "password, " +
                            "city, " +
                            "skills, " +
                            "comment, " +
                            "code, " +
                            "server, " +
                            "phone, " +
                            "email, " +
                            "approved, " +
                            "callback, " +
                            "verification_code, " +
                            "verification_type) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);",
                new Object[]{
                        player.getId(),
                        player.getFirstName(),
                        player.getLastName(),
                        player.getPassword(),
                        player.getCity(),
                        player.getSkills(),
                        player.getComment(),
                        player.getCode(),
                        player.getServer(),
                        player.getPhone(),
                        player.getEmail(),
                        player.getApproved(),
                        player.getCallback(),
                        player.getVerificationCode(),
                        player.getVerificationType(),
                });
    }

    public void update(Player player) {
        pool.update("UPDATE players SET " +
                        "first_name = ?, " +
                        "last_name = ?, " +
                        "password = ?, " +
                        "city = ?, " +
                        "skills = ?, " +
                        "comment = ?, " +
                        "code = ?, " +
                        "server = ?, " +
                        "phone = ?, " +
                        "email = ?, " +
                        "approved = ?, " +
                        "callback = ?, " +
                        "verification_code = ?, " +
                        "verification_type = ? " +
                        "WHERE id = ?;",
                new Object[]{
                        player.getFirstName(),
                        player.getLastName(),
                        player.getPassword(),
                        player.getCity(),
                        player.getSkills(),
                        player.getComment(),
                        player.getCode(),
                        player.getServer(),
                        player.getPhone(),
                        player.getEmail(),
                        player.getApproved(),
                        player.getCallback(),
                        player.getVerificationCode(),
                        player.getVerificationType(),
                        player.getId(),
                });
    }

    public void updateServer(String id, String server, String code) {
        pool.update("UPDATE players SET server = ?, code = ? WHERE id = ?;",
                server, code, id);
    }

    public void approve(String id) {
        pool.update("UPDATE players SET approved = ? WHERE id = ?;",
                Player.APPROVED, id);
    }

    public void updateVerificationCode(Player player, String verificationCode, VerificationType type) {
        String verificationType = (type == null) ? null : type.name();
        player.setVerificationType(verificationType);
        player.setVerificationCode(verificationCode);

        pool.update("UPDATE players SET verification_code = ?, verification_type = ? WHERE id = ?;",
                verificationCode, verificationType, player.getId());
    }

    public void remove(String id) {
        pool.update("DELETE FROM players WHERE id = ?;",
                new Object[]{id});
    }

    public void removeAll() {
        pool.update("DELETE FROM players;");
    }

    public Map<String, Player> getPlayersMap(List<String> ids) {
        return getPlayers(ids).stream()
                .collect(toMap(Player::getId, player -> player));
    }
}
