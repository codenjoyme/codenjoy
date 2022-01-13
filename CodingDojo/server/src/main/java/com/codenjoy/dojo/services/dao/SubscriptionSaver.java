package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;

import java.util.LinkedList;
import java.util.List;


public class SubscriptionSaver {

    private CrudConnectionThreadPool pool;

    public SubscriptionSaver(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS subscriptions (" +
                        "player_id varchar(255), " +
                        "query_id varchar(255)," +
                        "emailSubscription BOOLEAN," +
                        "slackSubscription BOOLEAN," +
                        "game_name varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public List<String> getUserQueriesForContest(String playerId, String game){
        return pool.select("SELECT * FROM subscriptions " +
                            "WHERE player_id = ? AND game_name = ?;",
                new Object[]{
                        playerId,
                        game
                },
                rs -> {
                    List<String> result = new LinkedList<>();
                    while (rs.next()) {
                        String id = rs.getString("query_id");
                        result.add(id);
                    }
                    return result;
                }
        );
    }

    public void saveSubscription(String playerId, int queryId, boolean email, boolean slack, String game) {
        pool.update("INSERT INTO subscriptions " +
                        "(player_id, query_id, emailSubscription, slackSubscription, game_name) " +
                        "VALUES (?,?,?,?,?);",
                new Object[]{playerId,
                        queryId,
                        email,
                        slack,
                        game
                });
    }

    public void tryDeleteSubscription(String playerId, String queryId, String game) {
        pool.update("DELETE FROM subscriptions " +
                        "WHERE player_id = ? AND query_id = ? AND game_name = ?;",
                new Object[]{
                        playerId,
                        queryId,
                        game
                });
    }

    public void updateEmailSubscription(String playerId, String queryId, boolean email, String game) {
        pool.update("UPDATE subscriptions " +
                        "SET emailSubscription = ? " +
                        "WHERE player_id = ? AND query_id = ? AND game_name = ?;",
                new Object[]{email,
                        playerId,
                        queryId,
                        game
                });
    }

    public void updateSlackSubscription(String playerId, String queryId, boolean slack, String game) {
        pool.update("UPDATE subscriptions " +
                        "SET slackSubscription = ? " +
                        "WHERE player_id = ? AND query_id = ? AND game_name = ?;",
                new Object[]{slack,
                        playerId,
                        queryId,
                        game
                });
    }

    public boolean getEmailValueForQuery(String playerId, String queryId, String game) {
        return pool.select("SELECT emailSubscription " +
                        "FROM subscriptions " +
                        "WHERE player_id = ? AND query_id = ? AND game_name = ?;",
                new Object[]{playerId,
                        queryId,
                        game},
                rs -> !rs.next() || rs.getBoolean("emailSubscription")
        );
    }

    public boolean getSlackValueForQuery(String playerId, String queryId, String game) {
        return pool.select("SELECT slackSubscription " +
                        "FROM subscriptions " +
                        "WHERE player_id = ? AND query_id = ? AND game_name = ?;",
                new Object[]{playerId,
                        queryId,
                        game},
                rs -> rs.next() && rs.getBoolean("slackSubscription")
        );
    }
}
