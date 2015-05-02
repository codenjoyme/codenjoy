package com.codenjoy.dojo.services.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ForStmt<T> {
    boolean run(PreparedStatement stmt, T parameter) throws SQLException;
}