package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2019 Codenjoy
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


import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scores {

    private CrudConnectionThreadPool pool;
     @Autowired protected ConfigProperties config;

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final DateTimeFormatter YYYY_MM_DD2 = DateTimeFormatter.ofPattern(YYYY_MM_DD);
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

//    public List<PlayerScore> getLeaders() {
//        String time = "19:00";
//        String firstDay = "2019-01-28";
//        int countWinners = 10;
//        String lastDay = "2019-02-10";
//
//        return pool.select("WITH RECURSIVE day_scores AS (\n" +
//                        "  SELECT * \n" +
//                        "    FROM scores \n" +
//                        "    INNER JOIN \n" +
//                        "        (SELECT MAX(time) AS max_time \n" +
//                        "            FROM (SELECT * FROM scores WHERE time LIKE '2019-%T?%') as test \n" +
//                        "            GROUP BY day) test \n" +
//                        "    ON time = max_time\n" +
//                        "), \n" +
//                        "\n" +
//                        "top_scores(emails, day) AS (\n" +
//                        "        (SELECT array_agg(row(email, day)) AS emails, '?' :: date AS day \n" +
//                        "            FROM (SELECT email, day FROM day_scores WHERE day = '?' ORDER BY score DESC LIMIT ?) initial_query) \n" +
//                        "    UNION ALL \n" +
//                        "        (SELECT * \n" +
//                        "            FROM \n" +
//                        "                (SELECT array_agg(row(email, s_day)) AS emails, MAX(ts_day):: date AS day \n" +
//                        "                    FROM \n" +
//                        "                        (SELECT ROW_NUMBER () OVER (PARTITION BY s.day ORDER BY score DESC) AS index, \n" +
//                        "                                time, score, ts.day AS ts_day, s.day AS s_day, email \n" +
//                        "                            FROM day_scores s, \n" +
//                        "                                (SELECT emails, top_scores.day + interval '1' day AS \"day\" \n" +
//                        "                                    FROM top_scores ORDER BY day DESC LIMIT 1) ts \n" +
//                        "                            WHERE ((NOT s.email IN (SELECT email FROM unnest(emails) AS (email text, day varchar))) \n" +
//                        "                                    AND s.day :: date = ts.day :: date) \n" +
//                        "                                OR (ts.day :: date != s.day :: date \n" +
//                        "                                    AND (s.email, s.day) = ANY(ts.emails))\n" +
//                        "                        ) indexed_top \n" +
//                        "                    WHERE index <= ?\n" +
//                        "                ) rec_exit \n" +
//                        "            WHERE day < '?')\n" +
//                        ") \n" +
//                        "     \n" +
//                        "SELECT DISTINCT nest_email AS name, nest_day AS day \n" +
//                        "    FROM top_scores, unnest(emails) AS (nest_email text, nest_day varchar)\n" +
//                        "    ORDER BY nest_day;\n",
//                new Object[]{time, firstDay, firstDay, countWinners, countWinners, lastDay},
//                rs -> buildScores(rs));
//    }

    public List<PlayerScore> getFinalists(String from, String to, long time,
                                          int finalistsCount, List<String> exclude)
    {
        List<String> finalists = new LinkedList<>();
        return getDaysBetween(from, to).stream()
                .map(day -> day.format(YYYY_MM_DD2))
                .filter(day -> isPast(day, time))
                .flatMap(day -> getScores(day, time).stream()
                    .sorted(Comparator.comparingInt(PlayerScore::getScore).reversed())
                    .filter(score -> !exclude.contains(score.getId()))
                    .filter(score -> !finalists.contains(score.getId()))
                    .limit(finalistsCount)
                    .map(score -> {
                        finalists.add(score.getId());
                        score.setDay(day);
                        return score;
                    })
            )
            .collect(Collectors.toList());
    }

    private List<LocalDate> getDaysBetween(String from, String to) {
        LocalDate start = LocalDate.parse(from, YYYY_MM_DD2);
        LocalDate end = LocalDate.parse(to, YYYY_MM_DD2);
        return Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end))
                .collect(Collectors.toList());
    }

    public List<PlayerScore> getScores(String day, long time) {
        if (isPast(day, time)) {
            time = getLastTimeOfPast(day);
        }

        return pool.select("SELECT * FROM scores WHERE time = ?;",
                new Object[]{JDBCTimeUtils.toString(new Date(time))},
                rs -> buildScores(rs));
    }

    private List<PlayerScore> buildScores(ResultSet rs) throws SQLException {
        return new LinkedList<PlayerScore>(){{
            while (rs.next()) {
                add(new PlayerScore(
                        rs.getString("email"),
                        rs.getInt("score")));
            }
        }};
    }

    public void removeByName(String email) {
        pool.update("DELETE FROM scores WHERE email = ?;",
                email);
    }

    public List<String> getDays() {
        return pool.select("SELECT DISTINCT day FROM scores;",
                new Object[0],
                rs -> new LinkedList<String>() {{
                    while (rs.next()) {
                        add(rs.getString(1));
                    }
                }});
    }

    public void removeByDay(String day) {
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

    public long getLastTimeOfPast(String day) {
        return pool.select("SELECT time FROM scores WHERE day = ? AND time LIKE ? ORDER BY time ASC LIMIT 1;",
                new Object[]{day, day + "T" + config.getGameFinalTime() + "%"},
                rs -> (rs.next()) ? JDBCTimeUtils.getTimeLong(rs) : 0);
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

    public void removeAll() {
        pool.update("DELETE FROM scores;");
    }
}
