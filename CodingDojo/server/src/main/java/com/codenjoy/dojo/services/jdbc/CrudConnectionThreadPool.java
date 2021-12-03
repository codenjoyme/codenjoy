package com.codenjoy.dojo.services.jdbc;

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


import com.codenjoy.dojo.web.controller.exception.UserRegistrationException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class CrudConnectionThreadPool extends ConnectionThreadPool {

    public CrudConnectionThreadPool(int count, Supplier<Connection> factory) {
        super(count, factory);
    }

    public <T> T select(String query, Object[] parameters, ObjectMapper<T> mapper) {
        return run(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                for (int index = 0; index < parameters.length; index++) {
                    stmt.setObject(index + 1, parameters[index]);
                }
                ResultSet resultSet = stmt.executeQuery();
                return mapper.mapFor(resultSet);
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Error when select '%s': %s", query, e));
            }
        });
    }

    public <T> T select(String query, ObjectMapper<T> mapper) {
        return select(query, new Object[0], mapper);
    }

    public int update(String query) {
        return update(query, new Object[0]);
    }

    public int registerUser(String query, Object[] parameters) throws UserRegistrationException {
        return run(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                for (int index = 0; index < parameters.length; index++) {
                    stmt.setObject(index + 1, parameters[index]);
                }
                return stmt.executeUpdate();
            } catch (SQLException e) {
                throw new UserRegistrationException(String.format("Error when update '%s': %s", query, e));
            }
        });
    }

    public int update(String query, Object[] parameters) {
        return run(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                for (int index = 0; index < parameters.length; index++) {
                    stmt.setObject(index + 1, parameters[index]);
                }
                return stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Error when update '%s': %s", query, e));
            }
        });
    }

    public <T> int[] batchUpdate(String query, List<T> parameters, ForStmt<T> forStmt) {
        return run(connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                for (T parameter : parameters) {
                    if (forStmt.run(stmt, parameter)) {
                        stmt.addBatch();
                    }
                }
                return stmt.executeBatch();
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Error when update '%s': %s", query, e));
            }
        });
    }

    public List<Object> batch(List<String> queries, List<Object[]> parameters, List<ObjectMapper<?>> mapper) {
        return run(connection -> {
            List<Object> result = new LinkedList<>();
            try {
                connection.setAutoCommit(false);
                for (int index = 0; index < queries.size(); index++) {
                    String query = queries.get(index);
                    Object[] objects = parameters.get(index);
                    ObjectMapper<?> objectMapper = (index < mapper.size()) ? mapper.get(index) : null;
                    try (PreparedStatement stmt = connection.prepareStatement(query)) {
                        for (int jndex = 0; jndex < objects.length; jndex++) {
                            stmt.setObject(jndex + 1, objects[jndex]);
                        }
                        if (query.toUpperCase().startsWith("SELECT")) {
                            stmt.addBatch();
                            ResultSet resultSet = stmt.executeQuery();
                            result.add((objectMapper == null) ? null : objectMapper.mapFor(resultSet));
                        } else {
                            int count = stmt.executeUpdate();
                            result.add(count);
                        }
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                throw new RuntimeException(String.format("Error when update '%s': %s", queries, e));
            } finally {
                try {
                    // т.к. конекшены шерятся между потоками, то надо возвращать как было в любом случае
                    connection.setAutoCommit(true);
                } catch (SQLException e2) {
                    throw new RuntimeException(String.format("Error when update '%s': %s", queries, e2));
                }
            }
            return result;
        });
    }
}
