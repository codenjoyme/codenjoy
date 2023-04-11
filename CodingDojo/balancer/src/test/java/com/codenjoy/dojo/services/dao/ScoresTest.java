package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.GameProperties;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.jdbc.JDBCTimeUtils;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

import static com.codenjoy.utils.DateUtils.day;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ScoresTest {

    private static Scores service;
    private List<String> excludedDays = new LinkedList<>();

    @Before
    public void setup() {
        // TODO продолжить с этим - проблема что на travis другая таймзона и все слетает
        JDBCTimeUtils.setTimeZone(TimeZone.getTimeZone("Europe/Kiev"));

        String dbFile = "target/scores.db" + new Random().nextInt();
        service = new Scores(
                new SqliteConnectionThreadPoolFactory(false, dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }))
        {{
            this.config = new ConfigProperties(){
                @Override
                public GameProperties getGame() {
                    GameProperties game = new GameProperties();
                    game.setFinalTime("19:00");
                    game.setExcludedDays(excludedDays);
                    return game;
                }
            };
        }};
    }

    @After
    public void tearDown() {
        service.removeDatabase();
    }

    @Test
    public void shouldNoScores_whenEmptyDb() {
        // when then
        assertEquals(service.getDays().toString(), "[]");
    }

    @Test
    @Ignore // TODO continue here
    public void shouldSetWinnerFlag() {
        // given
        String day = "2019-01-27";

        long time = day(day).plus(Calendar.SECOND, 10).get();

        service.saveScore(time, "stiven.pupkin.id", 1000, false);
        service.saveScore(time, "eva.pupkina.id", 2000, false);
        service.saveScore(time, "bob.marley.id", 3000, false);

        List<PlayerScore> scores = service.getScores(day, time);

        assertEquals("[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                        "PlayerScore{id='eva.pupkina.id', name='null', score=2000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                        "PlayerScore{id='bob.marley.id', name='null', score=3000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]",
                scores.toString());

        // when
        service.setWinnerFlag(scores, true);

        // then
        assertEquals("[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=true}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=true}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=true}]",
                service.getScores(day, time).toString());
    }

    @Test
    @Ignore // TODO continue here
    public void shouldSaveScores_forOneDay_andSeveralPlayers_once() {
        // given
        String day = "2019-01-27";

        long time = day(day).plus(Calendar.SECOND, 10).get();

        service.saveScore(time, "stiven.pupkin.id", 1000, false);
        service.saveScore(time, "eva.pupkina.id", 2000, false);
        service.saveScore(time, "bob.marley.id", 3000, false);

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27]");

        // последнее инфо дня
        long last = service.getLastTimeOf(day);
        assertEquals(last, time);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");
    }

    @Test
    @Ignore // TODO continue here
    public void shouldSaveScores_forOneDay_andSeveralPlayers_severalTimes() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).get();
        service.saveScore(time1, "stiven.pupkin.id", 1000, false);
        service.saveScore(time1, "eva.pupkina.id", 2000, false);

        long time2 = day(day).plus(Calendar.SECOND, 11).get();
        service.saveScore(time2, "stiven.pupkin.id", 1001, false);
        service.saveScore(time2, "eva.pupkina.id", 2001, false);
        service.saveScore(time2, "bob.marley.id", 3001, false);

        long time3 = day(day).plus(Calendar.SECOND, 12).get();
        service.saveScore(time3, "stiven.pupkin.id", 1002, false);
        service.saveScore(time3, "eva.pupkina.id", 2002, false);
        service.saveScore(time3, "bob.marley.id", 3002, false);

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27]");

        // последнее инфо дня
        long last = service.getLastTimeOf(day);
        assertEquals(last, time3);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1002, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:12.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2002, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:12.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3002, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:12.000+0200', server='null', winner=false}]");

        // кокретное инфо дня
        assertEquals(service.getScores(day, time1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");
    }

    @Test
    @Ignore // TODO continue here
    public void shouldSaveScores_forOneDay_andSeveralPlayers_severalTimes_batchUpdate() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).get();
        service.saveScores(time1, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1000"));
            add(new PlayerInfo("eva.pupkina.id", "2000"));
            add(new PlayerInfo("bob.marley.id", "3000"));
        }});

        long time2 = day(day).plus(Calendar.SECOND, 11).get();
        service.saveScores(time2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1001"));
            add(new PlayerInfo("eva.pupkina.id", "2001"));
            add(new PlayerInfo("bob.marley.id", "3001"));
            add(new PlayerInfo("apofig.id", "4001"));
        }});

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27]");

        // последнее инфо дня
        long last = service.getLastTimeOf(day);
        assertEquals(last, time2);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1001, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2001, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3001, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='apofig.id', name='null', score=4001, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}]");

        // конкретное инфо дня
        assertEquals(service.getScores(day, time1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");
    }

    @Test
    @Ignore // TODO continue here
    public void shouldSaveScores_forSeveralDays_andSeveralPlayers_severalTimes() {
        // given
        String day1 = "2019-01-27";

        long time1 = day(day1).plus(Calendar.SECOND, 10).get();
        service.saveScore(time1, "stiven.pupkin.id", 1000, false);
        service.saveScore(time1, "eva.pupkina.id", 2000, false);
        service.saveScore(time1, "bob.marley.id", 3000, false);

        // это время финального свистка, показывается из будущего в этот день
        long time19 = day(day1).plus(Calendar.HOUR, 19).get();
        service.saveScore(time19, "stiven.pupkin.id", 1111, false);
        service.saveScore(time19, "eva.pupkina.id", 2222, false);

        // а это время уже после 19:00
        long time2 = day(day1).plus(Calendar.HOUR, 20).get();
        service.saveScore(time2, "stiven.pupkin.id", 1222, false);
        service.saveScore(time2, "eva.pupkina.id", 2333, false);
        service.saveScore(time2, "bob.marley.id", 3444, false);

        String day2 = "2019-01-28";

        long time3 = day(day2).plus(Calendar.SECOND, 10).get();
        service.saveScore(time3, "stiven.pupkin.id", 1002, false);
        service.saveScore(time3, "eva.pupkina.id", 2002, false);
        service.saveScore(time3, "bob.marley.id", 3002, false);

        long time4 = day(day2).plus(Calendar.SECOND, 11).get();
        service.saveScore(time4, "stiven.pupkin.id", 1003, false);
        service.saveScore(time4, "eva.pupkina.id", 2003, false);
        service.saveScore(time4, "bob.marley.id", 3003, false);

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // последнее инфо прошлого дня
        long last1 = service.getLastTimeOf(day1);
        assertEquals(last1, time2);

        assertEquals(service.getScores(day1, last1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1222, lastHourScore=-, day='2019-01-27', time='2019-01-27T20:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2333, lastHourScore=-, day='2019-01-27', time='2019-01-27T20:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3444, lastHourScore=-, day='2019-01-27', time='2019-01-27T20:00:00.000+0200', server='null', winner=false}]");

        // конкретное инфо прошлого дня
        assertEquals(service.getScores(day1, time1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");

        // последнее инфо сегодняшнего дня
        long last2 = service.getLastTimeOf(day2);
        assertEquals(last2, time4);

        String lastInfoOfDay2 = "[PlayerScore{id='stiven.pupkin.id', name='null', score=1003, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2003, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3003, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}]";

        assertEquals(service.getScores(day2, last2).toString(),
                lastInfoOfDay2);

        // конкретное инфо сегодняшнего дня
        assertEquals(service.getScores(day2, time3).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1002, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2002, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3002, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:10.000+0200', server='null', winner=false}]");

        // а что если для текущего момента, но запрос сделали прошлого дня
        // возьмет последние данные за прошлый день
        assertEquals(service.getScores(day1, last2).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1111, lastHourScore=-, day='2019-01-27', time='2019-01-27T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2222, lastHourScore=-, day='2019-01-27', time='2019-01-27T19:00:00.000+0200', server='null', winner=false}]");
    }

    @Test
    public void shouldDeleteByDay() {
        // given
        String day1 = "2019-01-27";

        long time1 = day(day1).plus(Calendar.SECOND, 10).get();
        service.saveScore(time1, "stiven.pupkin.id", 1000, false);
        service.saveScore(time1, "eva.pupkina.id", 2000, false);
        service.saveScore(time1, "bob.marley.id", 3000, false);

        long time2 = day(day1).plus(Calendar.SECOND, 11).get();
        service.saveScore(time2, "stiven.pupkin.id", 1001, false);
        service.saveScore(time2, "eva.pupkina.id", 2001, false);
        service.saveScore(time2, "bob.marley.id", 3001, false);

        String day2 = "2019-01-28";

        long time3 = day(day2).plus(Calendar.SECOND, 10).get();
        service.saveScore(time3, "stiven.pupkin.id", 1002, false);
        service.saveScore(time3, "eva.pupkina.id", 2002, false);
        service.saveScore(time3, "bob.marley.id", 3002, false);

        long time4 = day(day2).plus(Calendar.SECOND, 11).get();
        service.saveScore(time4, "stiven.pupkin.id", 1003, false);
        service.saveScore(time4, "eva.pupkina.id", 2003, false);
        service.saveScore(time4, "bob.marley.id", 3003, false);

        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // when
        service.removeByDay(day2);

        // then
        assertEquals(service.getDays().toString(), "[2019-01-27]");
    }

    @Test
    @Ignore // TODO continue here
    public void shouldDeleteByName() {
        // given
        String day1 = "2019-01-27";

        long time1 = day(day1).plus(Calendar.SECOND, 10).get();
        service.saveScore(time1, "stiven.pupkin.id", 1000, false);
        service.saveScore(time1, "eva.pupkina.id", 2000, false);
        service.saveScore(time1, "bob.marley.id", 3000, false);

        long time2 = day(day1).plus(Calendar.SECOND, 11).get();
        service.saveScore(time2, "stiven.pupkin.id", 1001, false);
        service.saveScore(time2, "eva.pupkina.id", 2001, false);
        service.saveScore(time2, "bob.marley.id", 3001, false);

        String day2 = "2019-01-28";

        long time3 = day(day2).plus(Calendar.SECOND, 10).get();
        service.saveScore(time3, "stiven.pupkin.id", 1002, false);
        service.saveScore(time3, "eva.pupkina.id", 2002, false);
        service.saveScore(time3, "bob.marley.id", 3002, false);

        long time4 = day(day2).plus(Calendar.SECOND, 11).get();
        service.saveScore(time4, "stiven.pupkin.id", 1003, false);
        service.saveScore(time4, "eva.pupkina.id", 2003, false);
        service.saveScore(time4, "bob.marley.id", 3003, false);

        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // when
        service.remove("eva.pupkina.id");

        // then
        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // последнее инфо прошлого дня
        long last1 = service.getLastTimeOf(day1);
        assertEquals(last1, time2);

        assertEquals(service.getScores(day1, last1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1001, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3001, lastHourScore=-, day='2019-01-27', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}]");

        // последнее инфо последнего дня
        long last2 = service.getLastTimeOf(day2);
        assertEquals(last2, time4);

        assertEquals(service.getScores(day2, last2).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1003, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3003, lastHourScore=-, day='2019-01-28', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}]");
    }

    @Test
    public void shouldGetScores_forBrokenDay() {
        // given
        String day = "2019-01-27";

        long time = day(day).plus(Calendar.SECOND, 10).get();

        service.saveScore(time, "stiven.pupkin.id", 1000, false);
        service.saveScore(time, "eva.pupkina.id", 2000, false);
        service.saveScore(time, "bob.marley.id", 3000, false);

        // when then
        try {
            service.getScores("bla-bla", time);
            fail();
        } catch (RuntimeException e) {
            assertEquals("Unexpected day format, should be: yyyy-MM-dd", e.getMessage());
        }
    }

    @Test
    public void shouldGetLastTime() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).get();
        service.saveScore(time1, "stiven.pupkin.id", 1000, false);
        service.saveScore(time1, "eva.pupkina.id", 2000, false);

        long time2 = day(day).plus(Calendar.SECOND, 11).get();
        service.saveScore(time2, "stiven.pupkin.id", 1001, false);
        service.saveScore(time2, "eva.pupkina.id", 2001, false);
        service.saveScore(time2, "bob.marley.id", 3001, false);

        long time3 = day(day).plus(Calendar.SECOND, 12).get();
        service.saveScore(time3, "stiven.pupkin.id", 1002, false);
        service.saveScore(time3, "eva.pupkina.id", 2002, false);
        service.saveScore(time3, "bob.marley.id", 3002, false);

        // when
        long last = service.getLastTime(time1);

        // then
        assertEquals(last, time3);
    }

    @Test
    @Ignore // TODO continue here
    public void shouldGetFinalists() {
        // given
        excludedDays = Arrays.asList();

        long time1 = day("2019-01-27").plus(Calendar.HOUR, 19).get();
        service.saveScores(time1, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1001"));   // - не берем никого, дата не та
            add(new PlayerInfo("eva.pupkina.id", "2001"));
            add(new PlayerInfo("bob.marley.id", "3001"));
            // add(new PlayerInfo("apofig.id", "4002"));
            // add(new PlayerInfo("zanefig.id", "5002"));
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time2 = day("2019-01-28").plus(Calendar.HOUR, 19).get();
        service.saveScores(time2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1002"));
            add(new PlayerInfo("eva.pupkina.id", "2002"));
            add(new PlayerInfo("bob.marley.id", "3002"));      // +2 третье место, берем как второе
            add(new PlayerInfo("apofig.id", "4002"));          // -  второе место, дисквалифицирован
            add(new PlayerInfo("zanefig.id", "5002"));         // +1 первое место, берем
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time3 = day("2019-01-29").plus(Calendar.HOUR, 19).get();
        service.saveScores(time3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1003"));
            add(new PlayerInfo("eva.pupkina.id", "2003"));     // +2 четвертое место, берем как второе
            add(new PlayerInfo("bob.marley.id", "3003"));      // -  третье место, был вчера
            add(new PlayerInfo("apofig.id", "4003"));          // -  второе место, дисквалифицирован
            // add(new PlayerInfo("zanefig.id", "5003"));
            add(new PlayerInfo("nunafig.id", "6003"));         // +1 первое место, берем
        }});

        long time4 = day("2019-01-30").plus(Calendar.HOUR, 19).get();
        service.saveScores(time4, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1004"));    // +1 третье место, берем как второе
            // add(new PlayerInfo("eva.pupkina.id", "2004"));
            // add(new PlayerInfo("bob.marley.id", "3004"));
            add(new PlayerInfo("apofig.id", "4004"));           // -  второе место, дисквалицифирован
            // add(new PlayerInfo("zanefig.id", "5004"));
            add(new PlayerInfo("nunafig.id", "6004"));          // -  первое место, был вчера
        }});

        // все эти не будут включены так как не достигли 19:00 - день не закончился
        long time5 = day("2019-01-31").plus(Calendar.HOUR, 18).get();
        service.saveScores(time5, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1005"));     // - был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2005"));       // - был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3005"));        // - был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4005"));            // - дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5005"));           // - был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6005"));           // - был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7005"));           // - этот бы вошел, но не судьба
        }});

        // when then
        String expected = "PlayerScore{id='zanefig.id', name='null', score=5002, lastHourScore=-, day='2019-01-28', time='2019-01-28T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3002, lastHourScore=-, day='2019-01-28', time='2019-01-28T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='nunafig.id', name='null', score=6003, lastHourScore=-, day='2019-01-29', time='2019-01-29T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2003, lastHourScore=-, day='2019-01-29', time='2019-01-29T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='stiven.pupkin.id', name='null', score=1004, lastHourScore=-, day='2019-01-30', time='2019-01-30T19:00:00.000+0200', server='null', winner=false}";

        List<String> exclude = Arrays.asList("apofig.id");
        int finalistsCount = 2;
        assertEquals("[" + expected + "]",
            service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // даже если возьмем на день позже то все равно не получим те что в 18:00
        assertEquals("[" + expected +  "]",
        service.getFinalists("2019-01-28", "2019-02-01", finalistsCount, exclude).toString());

        // when then
        // а вот если ребята доиграют до 19:00 то все чик-чик
        long time5_2 = day("2019-01-31").plus(Calendar.HOUR, 19).get();
        service.saveScores(time5_2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1006"));     // -  был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2006"));       // -  был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3006"));        // -  был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4006"));            // -  дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5006"));           // -  был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6006"));           // -  был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7006"));           // +1  этот вошел
            add(new PlayerInfo("rerere.id", "1"));               // +2  войдет потому что 1 у него
            add(new PlayerInfo("rerere2.id", "0"));              // -  не войдет потому что 0 у него
        }});

        expected += ", PlayerScore{id='kukufig.id', name='null', score=7006, lastHourScore=-, day='2019-01-31', time='2019-01-31T19:00:00.000+0200', server='null', winner=false}" +
                    ", PlayerScore{id='rerere.id', name='null', score=1, lastHourScore=-, day='2019-01-31', time='2019-01-31T19:00:00.000+0200', server='null', winner=false}";
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // причем если они переиграют после 19, то это уже не будет влиять ни на что
        long time5_3 = day("2019-01-31").plus(Calendar.HOUR, 20).get();
        service.saveScores(time5_3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1007"));     // - был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2007"));       // - был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3007"));        // - был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4007"));            // - дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5007"));           // - был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6007"));           // - был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7007"));           // - этот бы вошел, но не судьба
        }});

        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // даже если будем брать следующий день в диапазоне
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-02-01", finalistsCount, exclude).toString());

    }

    @Test
    @Ignore // TODO continue here
    public void shouldGetFinalists_excludedDays() {
        // given
        excludedDays = Arrays.asList("2019-01-29", "2019-01-30");

        long time1 = day("2019-01-27").plus(Calendar.HOUR, 19).get();
        service.saveScores(time1, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1001"));   // - не берем никого, дата не та
            add(new PlayerInfo("eva.pupkina.id", "2001"));
            add(new PlayerInfo("bob.marley.id", "3001"));
            // add(new PlayerInfo("apofig.id", "4002"));
            // add(new PlayerInfo("zanefig.id", "5002"));
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time2 = day("2019-01-28").plus(Calendar.HOUR, 19).get();
        service.saveScores(time2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1002"));
            add(new PlayerInfo("eva.pupkina.id", "2002"));
            add(new PlayerInfo("bob.marley.id", "3002"));      // +2 третье место, берем как второе
            add(new PlayerInfo("apofig.id", "4002"));          // -  второе место, дисквалифицирован
            add(new PlayerInfo("zanefig.id", "5002"));         // +1 первое место, берем
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time3 = day("2019-01-29").plus(Calendar.HOUR, 19).get();
        service.saveScores(time3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1003"));
            add(new PlayerInfo("eva.pupkina.id", "2003"));     // - четвертое место, брали бы как второе, но этот день выходной
            add(new PlayerInfo("bob.marley.id", "3003"));      // - третье место, был вчера
            add(new PlayerInfo("apofig.id", "4003"));          // - второе место, дисквалифицирован
            // add(new PlayerInfo("zanefig.id", "5003"));
            add(new PlayerInfo("nunafig.id", "6003"));         // - первое место, брали бы, но этот день выходной
        }});

        long time4 = day("2019-01-30").plus(Calendar.HOUR, 19).get();
        service.saveScores(time4, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1004"));    // - третье место, брали бы как второе, но этот день выходной
            // add(new PlayerInfo("eva.pupkina.id", "2004"));
            // add(new PlayerInfo("bob.marley.id", "3004"));
            add(new PlayerInfo("apofig.id", "4004"));           // - второе место, дисквалицифирован
            // add(new PlayerInfo("zanefig.id", "5004"));
            add(new PlayerInfo("nunafig.id", "6004"));          // - первое место, был вчера
        }});

        // все эти не будут включены так как не достигли 19:00 - день не закончился
        long time5 = day("2019-01-31").plus(Calendar.HOUR, 18).get();
        service.saveScores(time5, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1005"));     // - был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2005"));       // - был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3005"));        // - был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4005"));            // - дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5005"));           // - был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6005"));           // - был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7005"));           // - этот бы вошел, но не судьба
        }});

        // when then
        String expected = "PlayerScore{id='zanefig.id', name='null', score=5002, lastHourScore=-, day='2019-01-28', time='2019-01-28T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3002, lastHourScore=-, day='2019-01-28', time='2019-01-28T19:00:00.000+0200', server='null', winner=false}";

        List<String> exclude = Arrays.asList("apofig.id");
        int finalistsCount = 2;
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // даже если возьмем на день позже то все равно не получим те что в 18:00
        assertEquals("[" + expected +  "]",
                service.getFinalists("2019-01-28", "2019-02-01", finalistsCount, exclude).toString());

        // when then
        // а вот если ребята доиграют до 19:00 то все чик-чик
        long time5_2 = day("2019-01-31").plus(Calendar.HOUR, 19).get();
        service.saveScores(time5_2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1006"));     // -  был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2006"));       // -  был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3006"));        // -  был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4006"));            // -  дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5006"));           // -  был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6006"));           // +2 был бы победителем в прошлом, если бы не выходной, потому берем сейчас
            add(new PlayerInfo("kukufig.id", "7006"));           // +1 этот вошел
            add(new PlayerInfo("rerere.id", "1"));               // -  не войдет сегодня потому что 1 у него, а вошел игрок с 6006
            add(new PlayerInfo("rerere2.id", "0"));              // -  не войдет потому что 0 у него
        }});

        expected += ", PlayerScore{id='kukufig.id', name='null', score=7006, lastHourScore=-, day='2019-01-31', time='2019-01-31T19:00:00.000+0200', server='null', winner=false}" +
                ", PlayerScore{id='nunafig.id', name='null', score=6006, lastHourScore=-, day='2019-01-31', time='2019-01-31T19:00:00.000+0200', server='null', winner=false}";
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // причем если они переиграют после 19, то это уже не будет влиять ни на что
        long time5_3 = day("2019-01-31").plus(Calendar.HOUR, 20).get();
        service.saveScores(time5_3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1007"));     // - был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2007"));       // - был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3007"));        // - был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4007"));            // - дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5007"));           // - был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6007"));           // - был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7007"));           // - этот бы вошел, но не судьба
        }});

        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // даже если будем брать следующий день в диапазоне
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-02-01", finalistsCount, exclude).toString());

    }

    @Test
    @Ignore // TODO continue here
    public void shouldGetFinalists_excludedDays_case2() {
        // given
        excludedDays = Arrays.asList("2019-01-28", "2019-01-29");

        long time1 = day("2019-01-27").plus(Calendar.HOUR, 19).get();
        service.saveScores(time1, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1001"));   // - не берем никого, дата не та
            add(new PlayerInfo("eva.pupkina.id", "2001"));
            add(new PlayerInfo("bob.marley.id", "3001"));
            // add(new PlayerInfo("apofig.id", "4002"));
            // add(new PlayerInfo("zanefig.id", "5002"));
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time2 = day("2019-01-28").plus(Calendar.HOUR, 19).get();
        service.saveScores(time2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1002"));
            add(new PlayerInfo("eva.pupkina.id", "2002"));
            add(new PlayerInfo("bob.marley.id", "3002"));      // -  третье место, брали бы как второе, если бы не выходной
            add(new PlayerInfo("apofig.id", "4002"));          // -  второе место, дисквалифицирован
            add(new PlayerInfo("zanefig.id", "5002"));         // -  первое место, брали бы, если бы не выходной
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time3 = day("2019-01-29").plus(Calendar.HOUR, 19).get();
        service.saveScores(time3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1003"));
            add(new PlayerInfo("eva.pupkina.id", "2003"));     // -  четвертое место, брали бы как второе, если бы не выходной
            add(new PlayerInfo("bob.marley.id", "3003"));      // -  третье место, был вчера
            add(new PlayerInfo("apofig.id", "4003"));          // -  второе место, дисквалифицирован
            // add(new PlayerInfo("zanefig.id", "5003"));
            add(new PlayerInfo("nunafig.id", "6003"));         // -  первое место, берали бы, если бы не выходной
        }});

        long time4 = day("2019-01-30").plus(Calendar.HOUR, 19).get();
        service.saveScores(time4, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1004"));    // -  не входит по очкам
            add(new PlayerInfo("eva.pupkina.id", "2004"));      // -  не входит по очкам
            add(new PlayerInfo("bob.marley.id", "3004"));       // -  не входит по очкам
            add(new PlayerInfo("apofig.id", "4004"));           // -  дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5004"));          // +2 второе место, хоть и был позавчера, но позавчера был выходной
            add(new PlayerInfo("nunafig.id", "6004"));          // +1 первое место, хоть и был вчера, но вчера был выходной
        }});

        // все эти не будут включены так как не достигли 19:00 - день не закончился
        long time5 = day("2019-01-31").plus(Calendar.HOUR, 18).get();
        service.saveScores(time5, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1005"));     // - не то время
            add(new PlayerInfo("eva.pupkina.id", "2005"));
            add(new PlayerInfo("bob.marley.id", "3005"));
            add(new PlayerInfo("apofig.id", "4005"));
            add(new PlayerInfo("zanefig.id", "5005"));
            add(new PlayerInfo("nunafig.id", "6005"));
            add(new PlayerInfo("kukufig.id", "7005"));
        }});

        // when then
        String expected =
                "PlayerScore{id='nunafig.id', name='null', score=6004, lastHourScore=-, day='2019-01-30', time='2019-01-30T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='zanefig.id', name='null', score=5004, lastHourScore=-, day='2019-01-30', time='2019-01-30T19:00:00.000+0200', server='null', winner=false}";

        List<String> exclude = Arrays.asList("apofig.id");
        int finalistsCount = 2;
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // даже если возьмем на день позже то все равно не получим те что в 18:00
        assertEquals("[" + expected +  "]",
                service.getFinalists("2019-01-28", "2019-02-01", finalistsCount, exclude).toString());

        // when then
        // а вот если ребята доиграют до 19:00 то все чик-чик
        long time5_2 = day("2019-01-31").plus(Calendar.HOUR, 19).get();
        service.saveScores(time5_2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1006"));     // -  не войдет, не добрал очков
            add(new PlayerInfo("eva.pupkina.id", "2006"));       // -  не войдет, не добрал очков
            add(new PlayerInfo("bob.marley.id", "3006"));        // +2 хоть и был победителем в 2019-01-28, но тогда был выходной, берем сейчас
            add(new PlayerInfo("apofig.id", "4006"));            // -  дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5006"));           // -  был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6006"));           // -  был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7006"));           // +1 этот вошел
            add(new PlayerInfo("rerere.id", "1"));               // -  не войдет, не добрал очков
            add(new PlayerInfo("rerere2.id", "0"));              // -  не войдет потому что 0 у него
        }});

        expected += ", PlayerScore{id='kukufig.id', name='null', score=7006, lastHourScore=-, day='2019-01-31', time='2019-01-31T19:00:00.000+0200', server='null', winner=false}" +
                ", PlayerScore{id='bob.marley.id', name='null', score=3006, lastHourScore=-, day='2019-01-31', time='2019-01-31T19:00:00.000+0200', server='null', winner=false}";
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // причем если они переиграют после 19, то это уже не будет влиять ни на что
        long time5_3 = day("2019-01-31").plus(Calendar.HOUR, 20).get();
        service.saveScores(time5_3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1007"));     // - был победителем в прошлом
            add(new PlayerInfo("eva.pupkina.id", "2007"));       // - был победителем в прошлом
            add(new PlayerInfo("bob.marley.id", "3007"));        // - был победителем в прошлом
            add(new PlayerInfo("apofig.id", "4007"));            // - дисквалицифирован
            add(new PlayerInfo("zanefig.id", "5007"));           // - был победителем в прошлом
            add(new PlayerInfo("nunafig.id", "6007"));           // - был победителем в прошлом
            add(new PlayerInfo("kukufig.id", "7007"));           // - этот бы вошел, но не судьба
        }});

        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-01-31", finalistsCount, exclude).toString());

        // when then
        // даже если будем брать следующий день в диапазоне
        assertEquals("[" + expected + "]",
                service.getFinalists("2019-01-28", "2019-02-01", finalistsCount, exclude).toString());
    }

    @Test
    @Ignore // TODO continue here
    public void getEarliestHourTime() {
        // GIVEN
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.MINUTE, 15).get();
        service.saveScores(time1, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1000"));
            add(new PlayerInfo("eva.pupkina.id", "2000"));
            add(new PlayerInfo("bob.marley.id", "3000"));
        }});

        long time2 = day(day).plus(Calendar.MINUTE, 30).get();
        service.saveScores(time2, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1001"));
            add(new PlayerInfo("eva.pupkina.id", "2001"));
            add(new PlayerInfo("bob.marley.id", "3001"));
            add(new PlayerInfo("apofig.id", "4001"));
        }});

        long time3 = day(day).plus(Calendar.MINUTE, 45).get();
        service.saveScores(time3, new LinkedList<>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1002"));
            add(new PlayerInfo("eva.pupkina.id", "2002"));
            add(new PlayerInfo("bob.marley.id", "3002"));
            add(new PlayerInfo("apofig.id", "4002"));
        }});

        // WHEN
        long earliestHourTime = service.getEarliestHourTime(day(day).get());

        // THEN
        assertEquals(time1, earliestHourTime);
    }
}
