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

import com.codenjoy.dojo.services.ContextPathGetter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class SqliteConnectionThreadPoolFactory implements ConnectionThreadPoolFactory {

    private String database;

    public SqliteConnectionThreadPoolFactory(boolean inMemory, String uri, ContextPathGetter context) {
        if (inMemory) {
            String name = StringUtils.substringBetween(uri, "/", ".db");
            database = String.format("file:%s?mode=memory&cache=shared", name);
        } else {
            database = uri.replace(".db", "_" + context.getContext() + ".db");
            createDirs(database);
        }
    }

    private void createDirs(String file) {
        File parent = new File(file).getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
    }

    @Override
    public CrudConnectionThreadPool create(String... createTableSqls) {
        return new SqliteConnectionThreadPool(database, createTableSqls);
    }
}
