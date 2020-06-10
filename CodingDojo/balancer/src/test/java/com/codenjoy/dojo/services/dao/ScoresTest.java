package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ScoresTest {

    private static Scores service;

    @Before
    public void setup() {
        String dbFile = "target/scores.db" + new Random().nextInt();
        service = new Scores(
                new SqliteConnectionThreadPoolFactory(dbFile,
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
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");
    }

    @Test
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
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1002, day='null', time='2019-01-27T00:00:12.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2002, day='null', time='2019-01-27T00:00:12.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3002, day='null', time='2019-01-27T00:00:12.000+0200', server='null', winner=false}]");

        // кокретное инфо дня
        assertEquals(service.getScores(day, time1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");
    }

    @Test
    public void shouldSaveScores_forOneDay_andSeveralPlayers_severalTimes_batchUpdate() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).get();
        service.saveScores(time1, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1000"));
            add(new PlayerInfo("eva.pupkina.id", "2000"));
            add(new PlayerInfo("bob.marley.id", "3000"));
        }});

        long time2 = day(day).plus(Calendar.SECOND, 11).get();
        service.saveScores(time2, new LinkedList<PlayerInfo>() {{
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
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1001, day='null', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2001, day='null', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3001, day='null', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='apofig.id', name='null', score=4001, day='null', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}]");

        // конкретное инфо дня
        assertEquals(service.getScores(day, time1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");
    }

    @Test
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
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1222, day='null', time='2019-01-27T20:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2333, day='null', time='2019-01-27T20:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3444, day='null', time='2019-01-27T20:00:00.000+0200', server='null', winner=false}]");

        // конкретное инфо прошлого дня
        assertEquals(service.getScores(day1, time1).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3000, day='null', time='2019-01-27T00:00:10.000+0200', server='null', winner=false}]");

        // последнее инфо сегодняшнего дня
        long last2 = service.getLastTimeOf(day2);
        assertEquals(last2, time4);

        String lastInfoOfDay2 = "[PlayerScore{id='stiven.pupkin.id', name='null', score=1003, day='null', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2003, day='null', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3003, day='null', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}]";

        assertEquals(service.getScores(day2, last2).toString(),
                lastInfoOfDay2);

        // конкретное инфо сегодняшнего дня
        assertEquals(service.getScores(day2, time3).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1002, day='null', time='2019-01-28T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2002, day='null', time='2019-01-28T00:00:10.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3002, day='null', time='2019-01-28T00:00:10.000+0200', server='null', winner=false}]");

        // а что если для текущего момента, но запрос сделали прошлого дня
        // возьмет последние данные за прошлый день
        assertEquals(service.getScores(day1, last2).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1111, day='null', time='2019-01-27T19:00:00.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='eva.pupkina.id', name='null', score=2222, day='null', time='2019-01-27T19:00:00.000+0200', server='null', winner=false}]");
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
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1001, day='null', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3001, day='null', time='2019-01-27T00:00:11.000+0200', server='null', winner=false}]");

        // последнее инфо последнего дня
        long last2 = service.getLastTimeOf(day2);
        assertEquals(last2, time4);

        assertEquals(service.getScores(day2, last2).toString(),
                "[PlayerScore{id='stiven.pupkin.id', name='null', score=1003, day='null', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}, " +
                "PlayerScore{id='bob.marley.id', name='null', score=3003, day='null', time='2019-01-28T00:00:11.000+0200', server='null', winner=false}]");
    }

    private static class ChangeCalendar {

        private Calendar calendar;

        public ChangeCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        public ChangeCalendar plus(int field, int amount) {
            calendar.add(field, amount);
            return this;
        }

        public long get() {
            return calendar.getTimeInMillis();
        }
    }

    private ChangeCalendar day(String day) {
        Date date = service.getDate(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new ChangeCalendar(calendar);
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
    public void shouldGetFinalists() {
        // given

        long time1 = day("2019-01-27").plus(Calendar.HOUR, 19).get();
        service.saveScores(time1, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1001"));   // не берем никого, дата не та
            add(new PlayerInfo("eva.pupkina.id", "2001"));
            add(new PlayerInfo("bob.marley.id", "3001"));
            // add(new PlayerInfo("apofig.id", "4002"));
            // add(new PlayerInfo("zanefig.id", "5002"));
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time2 = day("2019-01-28").plus(Calendar.HOUR, 19).get();
        service.saveScores(time2, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1002"));
            add(new PlayerInfo("eva.pupkina.id", "2002"));
            add(new PlayerInfo("bob.marley.id", "3002"));      // +2 третье место, берем как второе
            add(new PlayerInfo("apofig.id", "4002"));          // -  второе место, дисквалифицирован
            add(new PlayerInfo("zanefig.id", "5002"));         // +1 первое место, берем
            // add(new PlayerInfo("nunafig.id", "6003"));
        }});

        long time3 = day("2019-01-29").plus(Calendar.HOUR, 19).get();
        service.saveScores(time3, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1003"));
            add(new PlayerInfo("eva.pupkina.id", "2003"));     // +2 четвертое место, берем как второе
            add(new PlayerInfo("bob.marley.id", "3003"));      // -  третье место, был вчера
            add(new PlayerInfo("apofig.id", "4003"));          // -  второе место, дисквалифицирован
            // add(new PlayerInfo("zanefig.id", "5003"));
            add(new PlayerInfo("nunafig.id", "6003"));         // +1 первое место, берем
        }});

        long time4 = day("2019-01-30").plus(Calendar.HOUR, 19).get();
        service.saveScores(time4, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin.id", "1004"));    // +1 третье место, берем как второе
            // add(new PlayerInfo("eva.pupkina.id", "2004"));
            // add(new PlayerInfo("bob.marley.id", "3004"));
            add(new PlayerInfo("apofig.id", "4004"));           // -  второе место, дисквалицифирован
            // add(new PlayerInfo("zanefig.id", "5004"));
            add(new PlayerInfo("nunafig.id", "6004"));          // -  первое место, был вчера
        }});

        // when then
        long now = day("2019-01-31").plus(Calendar.HOUR, 19).get();
        List<String> exclude = Arrays.asList("apofig.id");
        int finalistsCount = 2;
        assertEquals(service.getFinalists("2019-01-28", "2019-01-31", now, finalistsCount, exclude).toString(),
            "[PlayerScore{id='zanefig.id', name='null', score=5002, day='2019-01-28', time='2019-01-28T19:00:00.000+0200', server='null', winner=false}, " +
            "PlayerScore{id='bob.marley.id', name='null', score=3002, day='2019-01-28', time='2019-01-28T19:00:00.000+0200', server='null', winner=false}, " +
            "PlayerScore{id='nunafig.id', name='null', score=6003, day='2019-01-29', time='2019-01-29T19:00:00.000+0200', server='null', winner=false}, " +
            "PlayerScore{id='eva.pupkina.id', name='null', score=2003, day='2019-01-29', time='2019-01-29T19:00:00.000+0200', server='null', winner=false}, " +
            "PlayerScore{id='stiven.pupkin.id', name='null', score=1004, day='2019-01-30', time='2019-01-30T19:00:00.000+0200', server='null', winner=false}]");
    }

}
