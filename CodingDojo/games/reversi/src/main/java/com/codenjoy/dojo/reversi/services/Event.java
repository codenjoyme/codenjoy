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

import com.codenjoy.dojo.services.event.EventObject;

import java.util.function.Function;

public class Event implements EventObject<Event.Type, Integer> {

    public static final Event WIN = new Event(Type.WIN, 1);
    public static final Event LOSE = new Event(Type.LOSE, 1);
    public static final Function<Integer, Event> FLIP = count -> new Event(Type.FLIP, count);

    private Type type;
    private int value;

    Event(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public enum Type {
        WIN,
        LOSE,
        FLIP
    }

    @Override
    public String toString() {
        return String.format("%s:%s", type, value);
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public Integer value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event events = (Event) o;

        if (value != events.value) return false;
        return type.equals(events.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + value;
        return result;
    }
}
