package com.codenjoy.dojo.services.jdbc;

/**
 * Created by indigo on 12.03.2015.
 */

import java.sql.Connection;

public interface For<T> {
    T run(Connection connection);
}