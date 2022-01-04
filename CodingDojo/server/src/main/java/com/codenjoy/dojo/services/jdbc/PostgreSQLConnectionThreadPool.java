package com.codenjoy.dojo.services.jdbc;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import java.sql.DriverManager;

public class PostgreSQLConnectionThreadPool extends CrudPrimaryKeyConnectionThreadPool {

    private static final int CONNECTIONS_COUNT = 10;

    public PostgreSQLConnectionThreadPool(String database, String... createTableQueries) {
        super(CONNECTIONS_COUNT, () -> getConnection(database));

        for (String query : createTableQueries) {
            createDB(query);
        }
    }

    @SneakyThrows
    private static Connection getConnection(String database) {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection("jdbc:postgresql://" + database);
    }

    public void removeDatabase() {
        close();
    }

    @Override
    public String getLastInsertedIdQuery(String table, String column) {
        return String.format("SELECT last_value FROM %s_%s_seq", table, column);
    }

    @Override
    protected String getPkDirective() {
        return "SERIAL PRIMARY KEY";
    }

    @Override
    protected String clearLastInsertedIdQuery(String table, String column) {
        return  String.format("ALTER SEQUENCE %s_%s_seq RESTART", table, column);
    }
}
