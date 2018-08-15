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


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ConnectionThreadPool {
    private ExecutorService executorService;

    private List<Connection> connections = new LinkedList<>();

    public ConnectionThreadPool(int count, final Get get) {
        executorService = Executors.newFixedThreadPool(count, runnable -> {
            Connection connection = null;
            try {
                connection = get.connection();
            } catch (Exception e) {
                throw new RuntimeException("Error get connection", e);
            }
            connections.add(connection);
            return new ConnectionThread(runnable, connection);
        });
    }

    public void close() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();
    }

    public <T> T run(final For<T> runner) {
        Future<T> submit = executorService.submit(() -> {
            ConnectionThread thread = (ConnectionThread) Thread.currentThread();
            Connection connection = thread.getConnection();
            return runner.run(connection);
        });

        try {
            return submit.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeDatabase() {
        // do nothing
    }
}
