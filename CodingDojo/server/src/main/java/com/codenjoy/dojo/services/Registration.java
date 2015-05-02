package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.jdbc.For;
import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        return pool.run(new For<String>() {
            @Override
            public String run(Connection connection) {
                String sql = "INSERT INTO users (email, email_approved, password, code) VALUES (?,?,?,?);";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setInt(2, 0);
                    stmt.setString(3, password);
                    stmt.setString(4, makeCode(email, password));
                    stmt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException("Error registering user: " + email, e);
                }
                return makeCode(email, password);
            }
        });
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

    public Void approve(final String code) {
        return pool.run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                String sql = "UPDATE users SET email_approved = ? WHERE code = ?;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, 1);
                    stmt.setString(2, code);
                    stmt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException("Error approve user with code: " + code, e);
                }
                return null;
            }
        });
    }
}
