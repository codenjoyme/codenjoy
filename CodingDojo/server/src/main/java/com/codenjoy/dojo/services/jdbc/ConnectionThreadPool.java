package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

public class ConnectionThreadPool {
    private ExecutorService executorService;

    private List<Connection> connections = new LinkedList<Connection>();

    public ConnectionThreadPool(int count, final Get get) {
        executorService = Executors.newFixedThreadPool(count, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Connection connection = null;
                try {
                    connection = get.connection();
                } catch (Exception e) {
                    throw new RuntimeException("Error get connection", e);
                }
                connections.add(connection);
                return new ConnectionThread(runnable, connection);
            }
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
        Future<T> submit = executorService.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                ConnectionThread thread = (ConnectionThread) Thread.currentThread();
                Connection connection = thread.getConnection();
                return runner.run(connection);
            }
        });

        try {
            return submit.get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
