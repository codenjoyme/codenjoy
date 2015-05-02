package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

}
