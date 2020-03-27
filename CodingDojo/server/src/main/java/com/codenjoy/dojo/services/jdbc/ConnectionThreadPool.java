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


import lombok.SneakyThrows;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class ConnectionThreadPool {

    private ExecutorService executorService;
    private List<Connection> connections = new LinkedList<>();

    public ConnectionThreadPool(int count, Supplier<Connection> factory) {
        executorService = Executors.newFixedThreadPool(count, openConnection(factory));
    }

    @SneakyThrows
    private ThreadFactory openConnection(Supplier<Connection> factory) {
        return runnable -> {
            Connection connection = factory.get();
            connections.add(connection);
            return new ConnectionThread(runnable, connection);
        };
    }

    @SneakyThrows
    public void close() {
        for (Connection connection : connections) {
            // TODO если один свалился, то хоть другие закрой, а?
            connection.close();
        }
        executorService.shutdown();
    }

    @SneakyThrows
    public <T> T run(final For<T> runner) {
        Future<T> submit = executorService.submit(() -> {
            ConnectionThread thread = (ConnectionThread) Thread.currentThread();
            Connection connection = thread.getConnection();
            return runner.run(connection);
        });

        return submit.get();
    }

    public void removeDatabase() {
        // do nothing
    }
}
