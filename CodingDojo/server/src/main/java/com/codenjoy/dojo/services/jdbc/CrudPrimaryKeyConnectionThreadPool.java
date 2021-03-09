package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;
import java.util.function.Supplier;

public abstract class CrudPrimaryKeyConnectionThreadPool extends CrudConnectionThreadPool {

    public CrudPrimaryKeyConnectionThreadPool(int count, Supplier<Connection> factory) {
        super(count, factory);
    }

    public Integer lastInsertId(String table, String column) {
        return select(getLastInsertedIdQuery(table, column),
                rs -> rs.next() ? rs.getInt(1) : null);
    }

    abstract String getLastInsertedIdQuery(String table, String column);

    void createDB(String sql) {
        sql = sql.replaceAll("integer_primary_key", getPkDirective());
        update(sql);
    }

    abstract String getPkDirective();

    abstract String clearLastInsertedIdQuery(String table, String column);

    public void clearLastInsertedId(String table, String column) {
        update(clearLastInsertedIdQuery(table, column));
    }
}
