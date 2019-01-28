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
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Component
public class Scores {

    private CrudConnectionThreadPool pool;

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    private SimpleDateFormat formatter = new SimpleDateFormat(YYYY_MM_DD);

    public Scores(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS scores (" +
                        "day varchar(10), " +
                        "time varchar(30), " +
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
                formatter.format(date),
                JDBCTimeUtils.toString(date),
                email,
                score);
    }

    public void saveScores(long time, List<PlayerInfo> playersInfos) {
        Date date = new Date(time);
        pool.batchUpdate("INSERT INTO scores " +
                        "(day, time, email, score) " +
                        "VALUES (?,?,?,?);",
                playersInfos,
                (PreparedStatement stmt, PlayerInfo info) -> {
                    pool.fillStatement(stmt,
                            formatter.format(date),
                            JDBCTimeUtils.toString(date),
                            info.getName(),
                            Integer.valueOf(info.getScore()));
                    return true;
                });
    }

    public List<PlayerScore> getScores(String day, long time) {
        if (isPast(day, time)) {
            time = getLastTimeOf(day);
        }

        // TODO а тут точно надо AND day = ?
        return pool.select("SELECT * FROM scores WHERE time = ? AND day = ?;",
                new Object[]{JDBCTimeUtils.toString(new Date(time)), day},
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

    public void deleteByName(String email) {
        pool.update("DELETE FROM scores WHERE email = ?;",
                email);
    }

    public List<String> getDays() {
        return pool.select("SELECT DISTINCT day FROM scores;",
                new Object[0],
                rs -> {
                    List<String> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(rs.getString(1));
                    }
                    return result;
                });
    }

    public void deleteByDay(String day) {
        pool.update("DELETE FROM scores WHERE day = ?;",
                day);
    }

    public long getLastTime(long time) {
        String day = getDay(time);
        return getLastTimeOf(day);
    }

    public String getDay(long time) {
        Date date = new Date(time);
        return formatter.format(date);
    }

    public long getLastTimeOf(String day) {
        return pool.select("SELECT time FROM scores WHERE day = ? ORDER BY time DESC LIMIT 1;",
                new Object[]{day},
                rs -> (rs.next()) ? JDBCTimeUtils.getTimeLong(rs) : 0);
    }

    public boolean isPast(String day, long lastTime) {
        Date date = getDate(getDay(lastTime));
        Date last = getDate(day);
        return last.before(date);
    }

    public Date getDate(String day) {
        try {
            return formatter.parse(day);
        } catch (ParseException e) {
            throw new RuntimeException("Unexpected day format, should be: " + YYYY_MM_DD, e);
        }
    }
}
