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


import com.codenjoy.dojo.services.Hash;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class Registration {

    private CrudConnectionThreadPool pool;

    public Registration(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "email varchar(255), " +
                        "email_approved int, " +
                        "password varchar(255)," +
                        "code varchar(255)," +
                        "data varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public boolean approved(final String email) {
        return pool.select("SELECT * FROM users WHERE email = ?;",
                new Object[]{email},
                new ObjectMapper<Boolean>() {
                    @Override
                    public Boolean mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getInt("email_approved") == 1;
                        } else {
                            return false;
                        }
                    }
                }
        );
    }

    public boolean registered(final String email) {
        return pool.select("SELECT count(*) AS total FROM users WHERE email = ?;",
                new Object[]{email},
                new ObjectMapper<Boolean>() {
                    @Override
                    public Boolean mapFor(ResultSet resultSet) throws SQLException {
                        if (!resultSet.next()) {
                            return false;
                        }
                        int count = resultSet.getInt("total");
                        if (count > 1) {
                            throw new IllegalStateException("Found more than one user with email " + email);
                        }
                        return count > 0;
                    }
                }
        );
    }

    public String register(final String email, final String password, String data) {
        String code = makeCode(email, password);
        pool.update("INSERT INTO users (email, email_approved, password, code, data) VALUES (?,?,?,?,?);",
                new Object[]{email, 0, password, code, data});
        return code;
    }

    public String login(final String email, final String password) {
        return pool.select("SELECT code FROM users WHERE email = ? AND password = ? AND email_approved != 0;",
                new Object[]{email, password},
                new ObjectMapper<String>() {
                    @Override
                    public String mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getString("code");
                        } else {
                            return null;
                        }
                    }
                }
        );
    }

    public static String makeCode(String email, String password) {
        return "" + Math.abs(email.hashCode()) + Math.abs(password.hashCode());
    }

    public boolean checkUser(String email, String code) {
        String actualName = getEmail(code);
        return actualName != null && actualName.equals(email);
    }

    public String getEmail(final String code) {
        return pool.select("SELECT email FROM users WHERE code = ?;",
                new Object[]{code},
                new ObjectMapper<String>() {
                    @Override
                    public String mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getString("email");
                        } else {
                            return null;
                        }
                    }
                }
        );
    }

    public String getCode(final String email) {
        return pool.select("SELECT code FROM users WHERE email = ?;",
                new Object[]{email},
                new ObjectMapper<String>() {
                    @Override
                    public String mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getString("code");
                        } else {
                            return null;
                        }
                    }
                }
        );
    }

    public void approve(final String code) {
        pool.update("UPDATE users SET email_approved = ? WHERE code = ?;",
                new Object[] {1, code});
    }

    class User {
        public User(String email, int email_approved, String password, String code, String data) {
            this.email = email;
            this.email_approved = email_approved;
            this.password = password;
            this.code = code;
            this.data = data;
        }

        String email;
        int email_approved;
        String password;
        String code;
        String data;

        @Override
        public String toString() {
            return "User{" +
                    "email='" + email + '\'' +
                    ", email_approved=" + email_approved +
                    ", password='" + password + '\'' +
                    ", code='" + code + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }
    }

    public void changePasswordsToMD5() {
        List<User> users = getUsers();

        for (User user : users) {
            pool.update("UPDATE users SET password = ? WHERE code = ?;",
                    new Object[]{Hash.md5(user.password), user.code});
        }
    }

    public List<User> getUsers() {
        return pool.select("SELECT * FROM users;",
                    new ObjectMapper<List<User>>() {
                        @Override
                        public List<User> mapFor(ResultSet resultSet) throws SQLException {
                            List<User> result = new LinkedList<User>();
                            while (resultSet.next()) {
                                result.add(new User(resultSet.getString("email"),
                                        resultSet.getInt("email_approved"),
                                        resultSet.getString("password"),
                                        resultSet.getString("code"),
                                        resultSet.getString("data")));
                            }
                            return result;
                        }
                    }
            );
    }
}
