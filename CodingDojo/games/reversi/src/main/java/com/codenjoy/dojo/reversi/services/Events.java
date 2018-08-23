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

public class Events {

    private static final String WIN = "WIN";
    private static final String LOOSE = "LOOSE";
    private static final String FLIP = "FLIP";

    private String name;
    private int count;

    public Events(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public static Events WIN() {
        return new Events(WIN, 1);
    }

    public static Events LOOSE() {
        return new Events(LOOSE, 1);
    }

    public static Events FLIP(int count) {
        return new Events(FLIP, count);
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

    public boolean isLoose() {
        return name.equals(LOOSE);
    }

    public boolean isWin() {
        return name.equals(WIN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Events events = (Events) o;

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
