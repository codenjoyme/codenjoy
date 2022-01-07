package com.codenjoy.utils;

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

import com.codenjoy.dojo.services.dao.Scores;

import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
    }

    public static ChangeCalendar day(String day) {
        Date date = Scores.getDate(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return new ChangeCalendar(calendar);
    }

    public static class ChangeCalendar {

        private final Calendar calendar;

        public ChangeCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        public ChangeCalendar plus(int field, int amount) {
            calendar.add(field, amount);
            return this;
        }

        public long get() {
            return calendar.getTimeInMillis();
        }
    }
}
