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

public class FeedbackSaver {

    private CrudConnectionThreadPool pool;

    public FeedbackSaver(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS feedback (" +
                        "player_id varchar(255), " +
                        "game_name varchar(255)," +
                        "feedback varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public void saveFeedback(String playerId, String game, String feedback) {
        pool.update("INSERT INTO feedback " +
                        "(player_id, game_name, feedback) " +
                        "VALUES (?,?,?);",
                new Object[]{
                        playerId,
                        game,
                        feedback
                });
    }
}
