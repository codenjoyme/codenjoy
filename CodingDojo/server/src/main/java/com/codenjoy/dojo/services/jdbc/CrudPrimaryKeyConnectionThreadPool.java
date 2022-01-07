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

import java.sql.Connection;
import java.util.function.Supplier;

public abstract class CrudPrimaryKeyConnectionThreadPool extends CrudConnectionThreadPool {

    public CrudPrimaryKeyConnectionThreadPool(int count, Supplier<Connection> factory) {
        super(count, factory);
    }

    public Integer lastInsertId(String table, String column) {
        return select(getLastInsertedIdQuery(table, column),
                rs -> rs.next() ? rs.getInt(1) + 1 : null);
    }

    /**
     * @return запрос который выдает для конкретной колонки {@param column}
     *         конкретной таблицы {@param table} последнюю сгенеренную айдишку
     */
    public abstract String getLastInsertedIdQuery(String table, String column);

    void createDB(String query) {
        query = query.replaceAll("integer_primary_key", getPkDirective());
        update(query);
    }

    /**
     * @return Название типа айдишки чтобы был аутоинкремент
     */
    protected abstract String getPkDirective();

    /**
     * @return запрос который очищает для конкретной колонки {@param column}
     *         конкретной таблицы {@param table} генератор айдишек
     */
    protected abstract String clearLastInsertedIdQuery(String table, String column);

    public void clearLastInsertedId(String table, String column) {
        update(clearLastInsertedIdQuery(table, column));
    }
}
