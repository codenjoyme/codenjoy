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
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteOpenMode;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class SqliteConnectionThreadPool extends CrudPrimaryKeyConnectionThreadPool {

    private static final int CONNECTIONS = 3;
    private String database;

    public SqliteConnectionThreadPool(String database, String... createTableQueries) {
        super(CONNECTIONS, () -> getConnection(database));

        this.database = database;

        for (String query : createTableQueries) {
            createDB(query);
        }
    }

    @SneakyThrows
    private static Connection getConnection(String database) {
        Class.forName("org.sqlite.JDBC");

        SQLiteConfig config = new SQLiteConfig();
        config.setOpenMode(SQLiteOpenMode.READWRITE);
        config.setOpenMode(SQLiteOpenMode.CREATE);
        config.setOpenMode(SQLiteOpenMode.NOMUTEX);

        return DriverManager.getConnection("jdbc:sqlite:" + database, config.toProperties());
    }

    public void removeDatabase() {
        close();

        File file = new File(database);
        if (!file.delete()) {
            throw new RuntimeException("Cant remove DB " + file.getAbsolutePath());
        }
    }

    @Override
    public String getLastInsertedIdQuery(String table, String column) {
        return "select last_insert_rowid()";
    }

    protected String getPkDirective() {
        return "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL";
    }

    @Override
    protected String clearLastInsertedIdQuery(String table, String column) {
        return String.format("UPDATE sqlite_sequence SET seq = 0 WHERE name = '%s'", table);
    }

    @Override
    public void createCluster(String table, String indexName) {
        // do nothing, there is not clusters in sqlite indexes
    }
}
