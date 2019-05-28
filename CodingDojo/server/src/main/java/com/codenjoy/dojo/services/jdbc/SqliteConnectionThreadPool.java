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


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteConnectionThreadPool extends CrudConnectionThreadPool {

    private static final int ONLY_ONE_CONNECTION = 1; // this is sqlite restriction
    private String databaseFile;

    public SqliteConnectionThreadPool(final String databaseFile, String... createTableSqls) {
        super(ONLY_ONE_CONNECTION, () -> {
            Class.forName("org.sqlite.JDBC");
            Connection result = DriverManager.getConnection("jdbc:sqlite:" + createDirs(databaseFile));
            return result;
        });
        this.databaseFile = databaseFile;

        for (String sql : createTableSqls) {
            createDB(sql);
        }
    }

    private static String createDirs(String databaseFile) {
        new File(databaseFile).getParentFile().mkdirs();
        return databaseFile;
    }

    private void createDB(final String sql) {
        update(sql);
    }

    public void removeDatabase() {
        close();

        File file = new File(databaseFile);
        if (!file.delete()) {
            throw new RuntimeException("Cant remove DB " + file.getAbsolutePath());
        }
    }
}
