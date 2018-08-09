package com.codenjoy.dojo.hex.services;

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

    private EventEnum event;
    private int count;

    // TODO подумать в контексте фреймворка как сделать так, чтобы любой ивент мог передавать параметры
    public enum EventEnum {
        WIN, LOOSE;
    }

    @Override
    public String toString() {
        return event + ((count != 0)?("(" + count + ")"):"");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (count != event.count) return false;
        if (this.event != event.event) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return count;
    }

    public Event(EventEnum event, int count) {
        this.event = event;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public EventEnum getType() {
        return event;
    }
}
