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


import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.sqlite.date.FastDateFormat;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@UtilityClass
public class JDBCTimeUtils {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final FastDateFormat formatter = FastDateFormat.getInstance(DATE_TIME_PATTERN);

    public static long getTimeLong(ResultSet resultSet) throws SQLException {
        try {
            // last version format
            String time = resultSet.getString("time");
            return getTimeLong(time);
        } catch (Exception e) {
            // TODO remove this block
            try {
                // for postgresql
                Time time = resultSet.getTime("time");
                return time.getTime();
            } catch (Exception e2) {
                // for sqlite
                return resultSet.getLong("time");
            }
        }
    }

    public static String toString(Date dateTime) {
        return formatter.format(dateTime);
    }

    @SneakyThrows
    public static long getTimeLong(String dateTime) {
        return formatter.parse(dateTime).getTime();
    }
}
