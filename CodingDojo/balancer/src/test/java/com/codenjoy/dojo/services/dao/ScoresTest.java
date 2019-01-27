package com.codenjoy.dojo.services.dao;

import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.testng.Assert.*;

public class ScoresTest {

    private static Scores service;

    @BeforeTest
    public void setup() {
        String dbFile = "target/scores.db" + new Random().nextInt();
        service = new Scores(
                new SqliteConnectionThreadPoolFactory(dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }));
    }

    @AfterTest
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
        service.saveScore(time1, "bob.marley@gmail.com", 3000);

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

        long last = service.getLastTimeOf(day);
        assertEquals(last, time3);

        assertEquals(service.getScores(day, last).toString(),
                "[PlayerScore{id='stiven.pupkin@gmail.com', name='null', score='1002', server='null'}, " +
                        "PlayerScore{id='eva.pupkina@gmail.com', name='null', score='2002', server='null'}, " +
                        "PlayerScore{id='bob.marley@gmail.com', name='null', score='3002', server='null'}]");
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

}