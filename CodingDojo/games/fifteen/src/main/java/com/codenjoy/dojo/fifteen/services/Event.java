package com.codenjoy.dojo.fifteen.services;

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

import com.codenjoy.dojo.services.event.EventObject;

public class Event implements EventObject<Event.Type, Event> {

    private Type type;
    private int moveCount;
    private int number;

    public enum Type {
        WIN,
        BONUS;
    }

    public Event(Type type) {
        this.type = type;
    }

    public Event(Type type, int moveCount, int number) {
        this.type = type;
        this.moveCount = moveCount;
        this.number = number;
    }

    public int number() {
        return number;
    }

    public int moveCount() {
        return moveCount;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return (type == Type.WIN)
                ? "Event[WIN]"
                : "Event[" +
                type +
                ", moveCount=" + moveCount +
                ", number=" + number +
                ']';
    }
}