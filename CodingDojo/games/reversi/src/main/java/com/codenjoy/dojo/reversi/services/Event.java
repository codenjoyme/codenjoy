package com.codenjoy.dojo.reversi.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

public class Event {

    private static final String WIN = "WIN";
    private static final String LOSE = "LOSE";
    private static final String FLIP = "FLIP";

    private String name;
    private int count;

    public Event(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public static Event WIN() {
        return new Event(WIN, 1);
    }

    public static Event LOSE() {
        return new Event(LOSE, 1);
    }

    public static Event FLIP(int count) {
        return new Event(FLIP, count);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", name, count);
    }

    public boolean isFlip() {
        return name.equals(FLIP);
    }

    public int count() {
        return count;
    }

    public boolean isLose() {
        return name.equals(LOSE);
    }

    public boolean isWin() {
        return name.equals(WIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event events = (Event) o;

        if (count != events.count) return false;
        return name.equals(events.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + count;
        return result;
    }
}
