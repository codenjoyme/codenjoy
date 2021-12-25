package com.codenjoy.dojo.snake.services;

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

    private Type type;
    private Integer value;

    public static final Event KILL = new Event(Type.KILL, 0);
    public static final Event EAT_STONE = new Event(Type.EAT_STONE, 0);
    public static final Function<Integer, Event> EAT_APPLE = value -> new Event(Type.EAT_APPLE, value);

    private Event(Type type, Integer value) {
        this.type = type;
        this.value = value;
    }

    enum Type {
        KILL,
        EAT_STONE,
        EAT_APPLE;
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
    public String toString() {
        return (type == Type.EAT_APPLE)
                ? "EAT_APPLE[" + value + "]"
                : type.name();
    }
}
