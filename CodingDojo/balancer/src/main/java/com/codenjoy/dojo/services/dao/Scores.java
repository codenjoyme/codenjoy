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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Scores {

    private CrudConnectionThreadPool pool;
    @Autowired protected ConfigProperties config;

    private static final String DAY_FORMAT = "yyyy-MM-dd";
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern(DAY_FORMAT);
    public static final SimpleDateFormat DAY_FORMATTER2 = new SimpleDateFormat(DAY_FORMAT);

    public Scores(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS scores (" +
                        "day varchar(10), " +
                        "time varchar(30), " +
                        "id varchar(255), " +
                        "score int," +
                        "winner int);");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    // TODO исправить тесты
    public void saveScore(long time, String id, int score, boolean winner) {
        Date date = new Date(time);
        pool.update("INSERT INTO scores " +
                        "(day, time, id, score, winner) " +
                        "VALUES (?,?,?,?,?);",
                DAY_FORMATTER2.format(date),
                JDBCTimeUtils.toString(date),
                id,
                score,
                winner ? 1 : 0);
    }

    public void saveScores(long time, List<PlayerInfo> playersInfos) {
        Date date = new Date(time);
        pool.batchUpdate("INSERT INTO scores " +
                        "(day, time, id, score, winner) " +
                        "VALUES (?,?,?,?,?);",
                playersInfos,
                (PreparedStatement stmt, PlayerInfo info) -> {
                    pool.fillStatement(stmt,
                            DAY_FORMATTER2.format(date),
                            JDBCTimeUtils.toString(date),
                            info.getId(),
                            Integer.valueOf(info.getScore()),
                            info.isWinner() ? 1 : 0);
                    return true;
                });
    }

    public List<PlayerScore> getFinalists(String from, String to,
                                          int finalistsCount,
                                          Collection<String> exclude)
    {
        to = plusDay(to);
        long time = parse(to);

        List<String> finalists = new LinkedList<>();
        return getDaysBetween(from, to).stream()
                .map(day -> day.format(Scores.DAY_FORMATTER))
                .filter(day -> isPast(day, time))
                .flatMap(day -> getScores(day, time).stream()
                    .filter(score -> score.getScore() > 0)
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

    private String plusDay(String day) {
        return LocalDate.parse(day, Scores.DAY_FORMATTER).plusDays(1).format(Scores.DAY_FORMATTER);
    }

    private long parse(String to) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getDate(to));
        return calendar.getTimeInMillis();
    }

    public void setWinnerFlag(PlayerScore playerScore, boolean isWinner) {
        pool.update("update scores set winner = ? where" +
                        " day = ? and time = ? and id = ? and score = ?",
                isWinner ? 1 : 0,
                playerScore.getDay(),
                playerScore.getTime(),
                playerScore.getId(),
                playerScore.getScore()
        );
    }

    public void cleanWinnerFlags() {
        pool.update("update scores set winner = 0 where winner <> 0");
    }

    private List<LocalDate> getDaysBetween(String from, String to) {
        LocalDate start = LocalDate.parse(from, DAY_FORMATTER);
        LocalDate end = LocalDate.parse(to, DAY_FORMATTER);
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
                        rs.getString("id"),
                        rs.getInt("score"),
                        rs.getString("time"),
                        rs.getInt("winner") == 1));
            }
        }};
    }

    public void remove(String id) {
        pool.update("DELETE FROM scores WHERE id = ?;", id);
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
        return DAY_FORMATTER2.format(date);
    }

    public long getLastTimeOfPast(String day) {
        return pool.select("SELECT time FROM scores WHERE day = ? AND time LIKE ? ORDER BY time ASC LIMIT 1;",
                new Object[]{day, day + "T" + config.getGame().getFinalTime() + "%"},
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
            return DAY_FORMATTER2.parse(day);
        } catch (ParseException e) {
            throw new RuntimeException("Unexpected day format, should be: " + DAY_FORMAT, e);
        }
    }

    public void removeAll() {
        pool.update("DELETE FROM scores;");
    }
}
