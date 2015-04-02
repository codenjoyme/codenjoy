package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionThread extends Thread {
    private Runnable task;
    private Connection connection;

    public ConnectionThread(Runnable task, Connection connection) {
        this.task = task;
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void run() {
        try {
            task.run();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}