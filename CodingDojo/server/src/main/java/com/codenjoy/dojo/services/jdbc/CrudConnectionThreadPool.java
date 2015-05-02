package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class CrudConnectionThreadPool extends ConnectionThreadPool {

    public CrudConnectionThreadPool(int count, final Get get) {
       super(count, get);
    }

    public <T> T select(final String query, final Object[] parameters, final ObjectMapper<T> mapper) {
        return run(new For<T>() {
            @Override
            public T run(Connection connection) {
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    for (int index = 0; index < parameters.length; index++) {
                        stmt.setObject(index + 1, parameters[index]);
                    }
                    ResultSet resultSet = stmt.executeQuery();
                    return mapper.mapFor(resultSet);
                } catch (SQLException e) {
                    throw new RuntimeException(String.format("Error when select '%s': %s", query, e));
                }
            }
        });
    }

    public <T> T select(final String query, final ObjectMapper<T> mapper) {
        return select(query, new Object[0], mapper);
    }

    public void update(final String query) {
        update(query, new Object[0]);
    }

    public void update(final String query, final Object[] parameters) {
        run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    for (int index = 0; index < parameters.length; index++) {
                        stmt.setObject(index + 1, parameters[index]);
                    }
                    stmt.execute();
                } catch (SQLException e) {
                    throw new RuntimeException(String.format("Error when update '%s': %s", query, e));
                }
                return null;
            }
        });
    }

    public <T> void batchUpdate(final String query, final List<T> parameters, final ForStmt<T> forStmt) {
        run(new For<Void>() {
            @Override
            public Void run(Connection connection) {
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    for (T parameter : parameters) {
                        if (forStmt.run(stmt, parameter)) {
                            stmt.addBatch();
                        }
                    }
                    stmt.executeBatch();
                } catch (SQLException e) {
                    throw new RuntimeException(String.format("Error when update '%s': %s", query, e));
                }
                return null;
            }
        });
    }
}
