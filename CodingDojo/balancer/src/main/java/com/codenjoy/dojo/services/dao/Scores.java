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


import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class Scores {

    private CrudConnectionThreadPool pool;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Scores(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS scores (" +
                        "day varchar(255), " +
                        "time varchar(255), " +
                        "email varchar(255), " +
                        "score int);");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public void saveScore(long time, String email, int score) {
        Date date = new Date(time);
        pool.update("INSERT INTO scores " +
                        "(day, time, email, score) " +
                        "VALUES (?,?,?,?);",
                new Object[]{
                        formatter.format(date),
                        JDBCTimeUtils.toString(date),
                        email,
                        score
                });
    }

    public List<PlayerScore> getScores(String day, long time) {
        if (isPast(day, time)) {
            time = getLastTimeOf(day);
        }

        return pool.select("SELECT * FROM scores WHERE day = ? AND time = ?;",
                new Object[]{day, JDBCTimeUtils.toString(new Date(time))},
                rs -> {
                    List<PlayerScore> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(new PlayerScore(
                                rs.getString("email"),
                                rs.getInt("score")));
                    }
                    return result;
                }
        );
    }

    public void delete(String name) {
        pool.update("DELETE FROM scores WHERE name = ?;",
                new Object[]{name});
    }

    public long getLastTime(long time) {
        String day = getDay(time);
        return getLastTimeOf(day);
    }

    private String getDay(long time) {
        Date date = new Date(time);
        return formatter.format(date);
    }

    public long getLastTimeOf(String day) {
        return pool.select("SELECT time FROM scores WHERE day = ? ORDER BY time DESC LIMIT 1;",
                new Object[]{day},
                rs -> (rs.next()) ? JDBCTimeUtils.getTimeLong(rs) : 0);
    }

    private boolean isPast(String day, long lastTime) {
        Date date = getDate(getDay(lastTime));
        Date last = getDate(day);
        if (last == null) {
            return true;
        }

        return last.before(date);
    }

    private Date getDate(String day) {
        try {
            return formatter.parse(day);
        } catch (ParseException e) {
            return null;
        }
    }
}
