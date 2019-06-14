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


import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.PostgreSQLConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class Payment {

    private CrudConnectionThreadPool pool;

    public Payment(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS payments (" +
                "email varchar(255), " +
                "game_type varchar(255), " +
                "till int);");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public boolean canPlay(final String email, final String gameType) {
        return pool.select("SELECT till FROM payments WHERE email = ? AND game_type = ?;",
                new Object[]{email, gameType},
                rs -> rs.next() && rs.getLong("till")
                        > Calendar.getInstance().getTime().getTime()
        );
    }

    public void buy(final String email, final String gameType, int days) {
        final Calendar calendar = Calendar.getInstance();
        long till = till(email, gameType);
        if (till != 0) {
            calendar.setTimeInMillis(till);
        }
        calendar.add(Calendar.DAY_OF_MONTH, days);

        if (till == 0) {
            pool.update("INSERT INTO payments (email, game_type, till) VALUES (?,?,?);",
                    new Object[]{email, gameType, calendar.getTime().getTime()});
        } else {
            pool.update("UPDATE payments SET till = ? WHERE email = ? AND game_type = ?;",
                    new Object[]{calendar.getTime().getTime(), email, gameType});
        }
    }

    public long till(String email, String gameType) {
        return pool.select("SELECT till FROM payments WHERE email = ? AND game_type = ?;",
                new Object[]{email, gameType},
                rs -> rs.next() ? rs.getLong("till") : 0L
        );
    }
}
