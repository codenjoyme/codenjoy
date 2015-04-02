package com.codenjoy.dojo.services.jdbc;

import java.sql.Connection;

/**
 * Created by indigo on 12.03.2015.
 */
public interface Get {
    Connection connection() throws Exception;
}
