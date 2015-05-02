package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.jdbc.For;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPool;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@Component
public class Registration {

    private SqliteConnectionThreadPool pool;

    public Registration(String dbFile) {
        pool = new SqliteConnectionThreadPool(dbFile,
                "CREATE TABLE IF NOT EXISTS users (" +
                        "email varchar(255), " +
                        "email_approved int, " +
                        "password varchar(255)," +
                        "code varchar(255));");
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
                        int count = resultSet.getInt("total");
                        if (count > 1) {
                            throw new IllegalStateException("Found more than one user with email " + email);
                        }
                        return count > 0;
                    }
                }
        );
    }

    public String register(final String email, final String password) {
        String code = makeCode(email, password);
        pool.update("INSERT INTO users (email, email_approved, password, code) VALUES (?,?,?,?);",
                new Object[]{email, 0, password, code});
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
        public User(String email, int email_approved, String password, String code) {
            this.email = email;
            this.email_approved = email_approved;
            this.password = password;
            this.code = code;
        }

        String email;
        int email_approved;
        String password;
        String code;
    }

    public void changePasswordsToMD5() {
        List<User> users = pool.select("SELECT * FROM users;",
                new ObjectMapper<List<User>>() {
                    @Override
                    public List<User> mapFor(ResultSet resultSet) throws SQLException {
                        List<User> result = new LinkedList<User>();
                        while (resultSet.next()) {
                            result.add(new User(resultSet.getString("email"),
                                    resultSet.getInt("email_approved"),
                                    resultSet.getString("password"),
                                    resultSet.getString("code")));
                        }
                        return result;
                    }
                }
        );

        for (User user : users) {
            pool.update("UPDATE users SET password = ? WHERE code = ?;",
                    new Object[]{md5(user.password), user.code});
        }
    }

    public String md5(String password) {
        return DigestUtils.md5DigestAsHex(password.getBytes());
    }
}
