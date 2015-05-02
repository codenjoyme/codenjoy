package com.codenjoy.dojo.services.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ObjectMapper<T> {

    T mapFor(ResultSet resultSet) throws SQLException;
}
