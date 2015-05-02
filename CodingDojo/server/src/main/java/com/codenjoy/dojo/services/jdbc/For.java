package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;

public interface For<T> {
    T run(Connection connection);
}