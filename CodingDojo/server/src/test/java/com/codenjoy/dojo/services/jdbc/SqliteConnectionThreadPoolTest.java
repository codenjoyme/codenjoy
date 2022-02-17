package com.codenjoy.dojo.services.jdbc;

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


import lombok.Data;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SqliteConnectionThreadPoolTest {

    private CrudPrimaryKeyConnectionThreadPool pool;

    private Map<Integer, Object[]> inserted = new HashMap<>();

    private static boolean sqlite = true; // false for testing in postgres
    private static final String POSTGRES_URL = "localhost:5432/codenjoy?user=codenjoy&password=localpwd";

    @Before
    public void setup() {
        String createQuery = "CREATE TABLE IF NOT EXISTS data (" +
                "id integer_primary_key, " +
                "username varchar(255), " +
                "password varchar(255));";

        if (sqlite) {
            String file = "target/pool.db" + new Random().nextInt();
            pool = new SqliteConnectionThreadPool(file, createQuery);
        } else {
            pool = new PostgreSQLConnectionThreadPool(
                    POSTGRES_URL, createQuery);
        }

        pool.clearLastInsertedId("data", "id");
        pool.update("DELETE FROM data");
    }

    @SneakyThrows
    @Test
    public void testMultiThreading_insertWithSelect() {
        // when
        int count = 20;
        doit(count, () -> createDummyRecord_withGetInsertedId());
        doit(count, () -> createDummyRecord());
        doit(count, () -> createDummyRecord_withGetInsertedId());
        doit(count, () -> readRecords());

        Thread.sleep(5000);

        // then
        List<User> list = getAllUsers();

        assertAllRecordsWithDifferentIds(count * 3, list);
        assertAllGeneratedIdsCorrespondsToDataInserted(count * 2, list);
    }

    public List<User> getAllUsers() {
        return pool.select("SELECT * FROM data " +
                        " ORDER BY id ASC;",
                new Object[]{},
                rs -> getUsers(rs));
    }

    private void assertAllGeneratedIdsCorrespondsToDataInserted(int count, List<User> list) {
        assertEquals(count, inserted.size());

        list.forEach(user -> {
            // TODO do not use map.containsKey just check that map.get() != null
            if (!inserted.containsKey(user.getId())) {
                return;
            }

            Object[] data = inserted.get(user.getId());
            assertEquals(user.getUsername(), data[0]);
            assertEquals(user.getPassword(), data[1]);
        });
    }

    private void assertAllRecordsWithDifferentIds(int count, List<User> list) {
        assertEquals(count, list.size());

        assertEquals(IntStream.range(1, count + 1)
                        .mapToObj(i -> i)
                        .collect(toList())
                        .toString(),

                list.stream()
                        .map(User::getId)
                        .collect(toList())
                        .toString());
    }

    @SneakyThrows
    private List<User> getUsers(ResultSet rs) {
        return new LinkedList<>() {{
            while (rs.next()) {
                add(new User(rs));
            }
        }};
    }

    @Data
    public static class User {
        private int id;
        private String username;
        private String password;

        private User(ResultSet rs) throws SQLException {
            id = rs.getInt("id");
            username = rs.getString("username");
            password = rs.getString("password");
        }
    }

    private void readRecords() {
        Integer result = pool.select("SELECT count(*) AS total FROM data;",
                rs -> rs.next() ? rs.getInt("total") : 0);
        assertTrue(result > 0);
        sleep(10);
    }

    private void sleep(int mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void createDummyRecord() {
        pool.update("INSERT INTO data (username, password) VALUES (?,?);",
                randomData());
        sleep(5);
    }

    private void createDummyRecord_withGetInsertedId() {
        Object[] data = randomData();

        List<Object> objects = pool.batch(Arrays.asList(
                "INSERT INTO data (username, password) VALUES (?,?);",
                pool.getLastInsertedIdQuery("data", "id")),

                Arrays.asList(data,
                        new Object[]{}),

                Arrays.asList(null,
                        rs -> rs.next() ? rs.getInt(1) : null));

        Integer id = (Integer) objects.get(1);

        inserted.put(id, data);

        sleep(5);
    }

    private Object[] randomData() {
        return new Object[]{
                String.valueOf(new Random().nextInt()),
                String.valueOf(new Random().nextInt())
        };
    }

    private Thread doit(int count, Runnable runnable) {
        Thread thread = new Thread(() -> {
            for (int index = 0; index < count; index++) {
                runnable.run();
            }
        });
        thread.start();
        return thread;
    }
}
