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


import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

//@Component
public class Registration {

    private CrudConnectionThreadPool pool;

    public Registration(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS users (" +
                        "email varchar(255), " +
                        "id varchar(255), " +
                        "readable_name varchar(255), " +
                        "email_approved int, " +
                        "password varchar(255)," +
                        "code varchar(255)," +
                        "data varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public boolean approved(String id) {
        return pool.select("SELECT * FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() && rs.getInt("email_approved") == 1
        );
    }

    public boolean registered(String id) {
        return pool.select("SELECT count(*) AS total FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> exists(rs, " id " + id)
        );
    }

    private Boolean exists(ResultSet rs, String details) throws SQLException {
        if (!rs.next()) {
            return false;
        }
        int count = rs.getInt("total");
        if (count > 1) {
            throw new IllegalStateException("Found more than one user with " + details);
        }
        return count > 0;
    }

    public String register(String id, String email, String readableName, String password, String data) {
        String code = Hash.getCode(id, password);
        pool.update("INSERT INTO users (id, email, readable_name, email_approved, password, code, data) VALUES (?,?,?,?,?,?,?);",
                new Object[]{id, email, readableName, 0, password, code, data});
        return code;
    }

    public String login(String id, String password) {
        return pool.select("SELECT code FROM users WHERE id = ? AND password = ? AND email_approved != 0;",
                new Object[]{id, password},
                rs -> rs.next() ? rs.getString("code") : null
        );
    }

    // TODO test me
    public String checkUser(String id) {
        if (getCodeById(id) != null) {
            return id;
        } else {
            return null;
        }
    }

    public String checkUser(String id, String code) {
        String stored = getIdByCode(code);

        if (stored == null) {
            return null;
        }

        if (stored.equals(id)) {
            return id;
        }

        return null;
    }

    // TODO test me
    public String checkUserByPassword(String id, String password) {
        return checkUser(id, Hash.getCode(id, password));
    }

    public String getIdByCode(String code) {
        return pool.select("SELECT id FROM users WHERE code = ?;",
                new Object[]{code},
                rs -> rs.next() ? rs.getString("id") : null
        );
    }

    // TODO test me
    public boolean emailIsUsed(String email) {
        return pool.select("SELECT count(*) AS total FROM users WHERE email = ?;",
                new Object[]{email},
                rs -> exists(rs, " email " + email)
        );
    }

    // TODO test me
    public boolean nameIsUsed(String name) {
        return pool.select("SELECT count(*) AS total FROM users WHERE readable_name = ?;",
                new Object[]{name},
                rs -> exists(rs, " name " + name)
        );
    }

    // TODO test me
    public String getEmailById(String id) {
        return pool.select("SELECT email FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("email") : null
        );
    }

    // TODO test me
    public String getIdByName(String name) {
        return pool.select("SELECT id FROM users WHERE readable_name = ?;",
                new Object[]{name},
                rs -> rs.next() ? rs.getString("id") : null
        );
    }

    // TODO test me
    public String getIdByEmail(String email) {
        return pool.select("SELECT id FROM users WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? rs.getString("id") : null
        );
    }

    public String getNameById(String id) {
        return pool.select("SELECT readable_name FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("readable_name") : null
        );
    }

    public String getCodeById(String id) {
        return pool.select("SELECT code FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("code") : null
        );
    }

    public void approve(String code) {
        pool.update("UPDATE users SET email_approved = ? WHERE code = ?;",
                new Object[]{1, code});
    }

    public void updateName(String id, String name) {
        pool.update("UPDATE users SET readable_name = ? WHERE id = ?;",
                new Object[]{name, id});
    }

    // TODO test me
    public void updateNameAndEmail(String id, String name, String email) {
        pool.update("UPDATE users SET readable_name = ?, email = ? WHERE id = ?;",
                new Object[]{name, email, id});
    }

    public static class User {
        private String email;
        private String id;
        private String readableName;
        private int approved;
        private String password;
        private String code;
        private String data;

        public User() {
            // do nothing
        }

        public User(String id, String email, String readableName, int approved, String password, String code, String data) {
            this.id = id;
            this.email = email;
            this.readableName = readableName;
            this.approved = approved;
            this.password = password;
            this.code = code;
            this.data = data;
        }

        public String getEmail() {
            return email;
        }

        public String getId() {
            return id;
        }

        public String getReadableName() {
            return readableName;
        }

        public int getApproved() {
            return approved;
        }

        public String getPassword() {
            return password;
        }

        public String getCode() {
            return code;
        }

        public String getData() {
            return data;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id='" + id + '\'' +
                    ", email=" + email +
                    ", readable_name=" + readableName +
                    ", email_approved=" + approved +
                    ", password='" + password + '\'' +
                    ", code='" + code + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }

        public void setCode(String code) {
            this.code = code;
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
                rs -> {
                    List<User> result = new LinkedList<>();
                    while (rs.next()) {
                        result.add(new User(
                                rs.getString("id"),
                                rs.getString("email"),
                                rs.getString("readable_name"),
                                rs.getInt("email_approved"),
                                rs.getString("password"),
                                rs.getString("code"),
                                rs.getString("data")));
                    }
                    return result;
                }
        );
    }

    public void replace(User user) {
        String code = user.getCode();
        if (StringUtils.isEmpty(code)) {
            code = Hash.getCode(user.getEmail(), user.getPassword());
        }

        Object[] parameters = {user.getReadableName(), user.getEmail(), 1, user.getPassword(), code, user.getData(), user.getId()};
        if (getCodeById(user.getId()) == null) {
            pool.update("INSERT INTO users (readable_name, email, email_approved, password, code, data, id) VALUES (?,?,?,?,?,?,?);",
                    parameters);
        } else {
            pool.update("UPDATE users SET readable_name = ?, email = ?, email_approved = ?, password = ?, code = ?, data = ? WHERE id = ?;",
                    parameters);
        }
    }

    public void remove(String id) {
        pool.update("DELETE FROM users WHERE id = ?;",
                new Object[]{id});
    }

    public void removeAll() {
        pool.update("DELETE FROM users;");
    }

}
