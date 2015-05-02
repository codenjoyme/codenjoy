package com.codenjoy.dojo.services.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by indigo on 02.05.2015.
 */
public interface ObjectMapper<T> {

    T mapFor(ResultSet resultSet) throws SQLException;
}
