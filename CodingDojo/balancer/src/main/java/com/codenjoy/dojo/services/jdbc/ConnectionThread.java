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
            close();
        }
    }

    @SneakyThrows
    private void close() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }
}
