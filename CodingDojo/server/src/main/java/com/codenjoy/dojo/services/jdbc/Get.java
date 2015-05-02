package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;

public interface Get {
    Connection connection() throws Exception;
}
