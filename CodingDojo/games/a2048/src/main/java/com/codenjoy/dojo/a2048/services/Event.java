package com.codenjoy.dojo.a2048.services;

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

public class Event implements EventObject<Event.Type, Integer> {

    private Type event;
    private int number;

    public enum Type {
        SUM,
        GAME_OVER,
        WIN;
    }

    @Override
    public String toString() {
        return event + ((number != 0)?("(" + number + ")"):"");
    }

    public Event(Type event) {
        this.event = event;
    }

    public Event(Type event, int number) {
        this.event = event;
        this.number = number;
    }

    @Override
    public Integer value() {
        return number;
    }

    @Override
    public Type type() {
        return event;
    }
}