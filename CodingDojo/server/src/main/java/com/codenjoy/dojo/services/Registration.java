package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.jdbc.For;
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
        return pool.run(new For<Boolean>() {
            @Override
            public Boolean run(Connection connection) {
                String sql = "SELECT * FROM users WHERE email = ?;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getInt("email_approved") == 1;
                    } else {
                        return false;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error when is_approved user: " + email, e);
                }
            }
        });
    }

    public boolean registered(final String email) {
        return pool.run(new For<Boolean>() {
            @Override
            public Boolean run(Connection connection) {
                String sql = "SELECT count(*) AS total FROM users WHERE email = ?;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    ResultSet resultSet = stmt.executeQuery();
                    int count = resultSet.getInt("total");
                    if (count > 1) {
                        throw new IllegalStateException("Found more than one user with email " + email);
                    }
                    return count > 0;
                } catch (SQLException e) {
                    throw new RuntimeException("Error when is_registered user: " + email, e);
                }
            }
        });
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
        return pool.run(new For<String>() {
            @Override
            public String run(Connection connection) {
                String sql = "SELECT code FROM users WHERE email = ? AND password = ? AND email_approved != 0;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    stmt.setString(2, password);
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("code");
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error login user: " + email, e);
                }
            }
        });
    }

    public static String makeCode(String email, String password) {
        return "" + Math.abs(email.hashCode()) + Math.abs(password.hashCode());
    }

    public String getEmail(final String code) {
        return pool.run(new For<String>() {
            @Override
            public String run(Connection connection) {
                String sql = "SELECT email FROM users WHERE code = ?;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, code);
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("email");
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error get email by code: " + code, e);
                }
            }
        });
    }

    public String getCode(final String email) {
        return pool.run(new For<String>() {
            @Override
            public String run(Connection connection) {
                String sql = "SELECT code FROM users WHERE email = ?;";

                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setString(1, email);
                    ResultSet resultSet = stmt.executeQuery();
                    if (resultSet.next()) {
                        return resultSet.getString("code");
                    } else {
                        return null;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Error get code by email: " + email, e);
                }
            }
        });
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
