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
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.testng.annotations.*;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import static org.testng.Assert.*;

public class ScoresTest {

    private static Scores service;

    @BeforeMethod
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
            this.properties = new ConfigProperties(){
                @Override
                public String getGameFinalTime() {
                    return "19:00";
                }
            };
        }};
    }

    @AfterMethod
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

        long time = day(day).plus(Calendar.SECOND, 10).getTimeInMillis();

        service.saveScore(time, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time, "eva.pupkina@gmail.com", 2000);
        service.saveScore(time, "bob.marley@gmail.com", 3000);

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27]");

        // последнее инфо дня
        long last = service.getLastTimeOf(day);
        assertEquals(last, time);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1000', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2000', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3000', server='null'}]");
    }

    @Test
    public void shouldSaveScores_forOneDay_andSeveralPlayers_severalTimes() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time1, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time1, "eva.pupkina@gmail.com", 2000);

        long time2 = day(day).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time2, "stiven.pupkin@gmail.com", 1001);
        service.saveScore(time2, "eva.pupkina@gmail.com", 2001);
        service.saveScore(time2, "bob.marley@gmail.com", 3001);

        long time3 = day(day).plus(Calendar.SECOND, 12).getTimeInMillis();
        service.saveScore(time3, "stiven.pupkin@gmail.com", 1002);
        service.saveScore(time3, "eva.pupkina@gmail.com", 2002);
        service.saveScore(time3, "bob.marley@gmail.com", 3002);

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27]");

        // последнее инфо дня
        long last = service.getLastTimeOf(day);
        assertEquals(last, time3);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1002', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2002', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3002', server='null'}]");

        // кокретное инфо дня
        assertEquals(service.getScores(day, time1).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1000', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2000', server='null'}]");
    }

    @Test
    public void shouldSaveScores_forOneDay_andSeveralPlayers_severalTimes_batchUpdate() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScores(time1, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin@gmail.com", "1000"));
            add(new PlayerInfo("eva.pupkina@gmail.com", "2000"));
            add(new PlayerInfo("bob.marley@gmail.com", "3000"));
        }});

        long time2 = day(day).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScores(time2, new LinkedList<PlayerInfo>() {{
            add(new PlayerInfo("stiven.pupkin@gmail.com", "1001"));
            add(new PlayerInfo("eva.pupkina@gmail.com", "2001"));
            add(new PlayerInfo("bob.marley@gmail.com", "3001"));
            add(new PlayerInfo("apofig@gmail.com", "4001"));
        }});

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27]");

        // последнее инфо дня
        long last = service.getLastTimeOf(day);
        assertEquals(last, time2);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1001', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2001', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3001', server='null'}, " +
                        "PlayerScore{id='apofig@gmail.com', name='null', score='4001', server='null'}]");

        // конкретное инфо дня
        assertEquals(service.getScores(day, time1).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1000', server='null'}, " +
                "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2000', server='null'}, " +
                "PlayerScore{id='bob.marley@gmail.com', name='null', score='3000', server='null'}]");
    }

    @Test
    public void shouldSaveScores_forSeveralDays_andSeveralPlayers_severalTimes() {
        // given
        String day1 = "2019-01-27";

        long time1 = day(day1).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time1, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time1, "eva.pupkina@gmail.com", 2000);
        service.saveScore(time1, "bob.marley@gmail.com", 3000);

        // это время финального свистка, показывается из будущего в этот день
        long time19 = day(day1).plus(Calendar.HOUR, 19).getTimeInMillis();
        service.saveScore(time19, "stiven.pupkin@gmail.com", 1111);
        service.saveScore(time19, "eva.pupkina@gmail.com", 2222);

        // а это время уже после 19:00
        long time2 = day(day1).plus(Calendar.HOUR, 20).getTimeInMillis();
        service.saveScore(time2, "stiven.pupkin@gmail.com", 1222);
        service.saveScore(time2, "eva.pupkina@gmail.com", 2333);
        service.saveScore(time2, "bob.marley@gmail.com", 3444);

        String day2 = "2019-01-28";

        long time3 = day(day2).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time3, "stiven.pupkin@gmail.com", 1002);
        service.saveScore(time3, "eva.pupkina@gmail.com", 2002);
        service.saveScore(time3, "bob.marley@gmail.com", 3002);

        long time4 = day(day2).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time4, "stiven.pupkin@gmail.com", 1003);
        service.saveScore(time4, "eva.pupkina@gmail.com", 2003);
        service.saveScore(time4, "bob.marley@gmail.com", 3003);

        // when then
        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // последнее инфо прошлого дня
        long last1 = service.getLastTimeOf(day1);
        assertEquals(last1, time2);

        assertEquals(service.getScores(day1, last1).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1222', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2333', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3444', server='null'}]");

        // конкретное инфо прошлого дня
        assertEquals(service.getScores(day1, time1).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1000', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2000', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3000', server='null'}]");

        // последнее инфо сегодняшнего дня
        long last2 = service.getLastTimeOf(day2);
        assertEquals(last2, time4);

        String lastInfoOfDay2 = "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1003', server='null'}, " +
                "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2003', server='null'}, " +
                "PlayerScore{id='bob.marley@gmail.com', name='null', score='3003', server='null'}]";

        assertEquals(service.getScores(day2, last2).toString(),
                lastInfoOfDay2);

        // конкретное инфо сегодняшнего дня
        assertEquals(service.getScores(day2, time3).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1002', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2002', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3002', server='null'}]");

        // а что если для текущего момента, но запрос сделали прошлого дня
        // возьмет последние данные за прошлый день
        assertEquals(service.getScores(day1, last2).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1111', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2222', server='null'}]");
    }

    @Test
    public void shouldDeleteByDay() {
        // given
        String day1 = "2019-01-27";

        long time1 = day(day1).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time1, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time1, "eva.pupkina@gmail.com", 2000);
        service.saveScore(time1, "bob.marley@gmail.com", 3000);

        long time2 = day(day1).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time2, "stiven.pupkin@gmail.com", 1001);
        service.saveScore(time2, "eva.pupkina@gmail.com", 2001);
        service.saveScore(time2, "bob.marley@gmail.com", 3001);

        String day2 = "2019-01-28";

        long time3 = day(day2).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time3, "stiven.pupkin@gmail.com", 1002);
        service.saveScore(time3, "eva.pupkina@gmail.com", 2002);
        service.saveScore(time3, "bob.marley@gmail.com", 3002);

        long time4 = day(day2).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time4, "stiven.pupkin@gmail.com", 1003);
        service.saveScore(time4, "eva.pupkina@gmail.com", 2003);
        service.saveScore(time4, "bob.marley@gmail.com", 3003);

        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // when
        service.deleteByDay(day2);

        // then
        assertEquals(service.getDays().toString(), "[2019-01-27]");
    }

    @Test
    public void shouldDeleteByName() {
        // given
        String day1 = "2019-01-27";

        long time1 = day(day1).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time1, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time1, "eva.pupkina@gmail.com", 2000);
        service.saveScore(time1, "bob.marley@gmail.com", 3000);

        long time2 = day(day1).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time2, "stiven.pupkin@gmail.com", 1001);
        service.saveScore(time2, "eva.pupkina@gmail.com", 2001);
        service.saveScore(time2, "bob.marley@gmail.com", 3001);

        String day2 = "2019-01-28";

        long time3 = day(day2).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time3, "stiven.pupkin@gmail.com", 1002);
        service.saveScore(time3, "eva.pupkina@gmail.com", 2002);
        service.saveScore(time3, "bob.marley@gmail.com", 3002);

        long time4 = day(day2).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time4, "stiven.pupkin@gmail.com", 1003);
        service.saveScore(time4, "eva.pupkina@gmail.com", 2003);
        service.saveScore(time4, "bob.marley@gmail.com", 3003);

        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // when
        service.deleteByName("eva.pupkina@gmail.com");

        // then
        assertEquals(service.getDays().toString(), "[2019-01-27, 2019-01-28]");

        // последнее инфо прошлого дня
        long last1 = service.getLastTimeOf(day1);
        assertEquals(last1, time2);

        assertEquals(service.getScores(day1, last1).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1001', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3001', server='null'}]");

        // последнее инфо последнего дня
        long last2 = service.getLastTimeOf(day2);
        assertEquals(last2, time4);

        assertEquals(service.getScores(day2, last2).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1003', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3003', server='null'}]");
    }

    private static class ChangeCalendar {

        private Calendar calendar;

        public ChangeCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        public Calendar plus(int field, int amount) {
            calendar.add(field, amount);
            return calendar;
        }
    }

    private ChangeCalendar day(String day) {
        Date date = service.getDate(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new ChangeCalendar(calendar);
    }

    @Test(expectedExceptions = { RuntimeException.class },
          expectedExceptionsMessageRegExp = "Unexpected day format, should be: yyyy-MM-dd")
    public void shouldGetScores_forBrokenDay() {
        // given
        String day = "2019-01-27";

        long time = day(day).plus(Calendar.SECOND, 10).getTimeInMillis();

        service.saveScore(time, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time, "eva.pupkina@gmail.com", 2000);
        service.saveScore(time, "bob.marley@gmail.com", 3000);

        // when then
        service.getScores("bla-bla", time);
    }

    @Test
    public void shouldGetLastTime() {
        // given
        String day = "2019-01-27";

        long time1 = day(day).plus(Calendar.SECOND, 10).getTimeInMillis();
        service.saveScore(time1, "stiven.pupkin@gmail.com", 1000);
        service.saveScore(time1, "eva.pupkina@gmail.com", 2000);

        long time2 = day(day).plus(Calendar.SECOND, 11).getTimeInMillis();
        service.saveScore(time2, "stiven.pupkin@gmail.com", 1001);
        service.saveScore(time2, "eva.pupkina@gmail.com", 2001);
        service.saveScore(time2, "bob.marley@gmail.com", 3001);

        long time3 = day(day).plus(Calendar.SECOND, 12).getTimeInMillis();
        service.saveScore(time3, "stiven.pupkin@gmail.com", 1002);
        service.saveScore(time3, "eva.pupkina@gmail.com", 2002);
        service.saveScore(time3, "bob.marley@gmail.com", 3002);

        // when
        long last = service.getLastTime(time1);

        // then
        assertEquals(last, time3);
    }

}
