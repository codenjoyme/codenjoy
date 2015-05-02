package com.codenjoy.dojo.services.jdbc;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by indigo on 12.03.2015.
 */
public class SqliteConnectionThreadPoolTest {

    private SqliteConnectionThreadPool pool;

    @Test
    public void testMultiThreading() throws InterruptedException {
        try {
            String file = "target/pool.db" + new Random().nextInt();
            pool = new SqliteConnectionThreadPool(file,
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "user varchar(255), " +
                            "password varchar(255));");

            doit(10, new Runnable() {
                @Override
                public void run() {
                    createDummyRecord();
                }
            });

            doit(10, new Runnable() {
                @Override
                public void run() {
                    createDummyRecord();
                }
            });

            doit(10, new Runnable() {
                @Override
                public void run() {
                    readRecords();
                }
            });

            Thread.sleep(5000);
        } finally {
//            pool.removeDatabase();
        }
    }

    private void readRecords() {
        Integer result = pool.select("SELECT count(*) AS total FROM users;",
                new ObjectMapper<Integer>() {
                    @Override
                    public Integer mapFor(ResultSet resultSet) throws SQLException {
                        return resultSet.getInt("total");
                    }
                });

        sleep(10);
        System.out.println(result);
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createDummyRecord() {
        pool.update("INSERT INTO users (user, password) VALUES (?,?);",
                new Object[] {
                        String.valueOf(new Random().nextInt()),
                        String.valueOf(new Random().nextInt())
                });
        sleep(10);
    }

    private Thread doit(final int count, final Runnable runnable) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    runnable.run();
                }
            }
        });
        thread.start();
        return thread;
    }
}
