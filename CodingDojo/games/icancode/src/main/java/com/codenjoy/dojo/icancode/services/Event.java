package com.codenjoy.dojo.icancode.services;

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

public class Event implements EventObject<Event.Type, Event> {

    private Type type;

    private int goldCount;
    private boolean multiple;
    private int killCount;

    public static Event WIN(int goldCount, boolean multiple) {
        return new Event(goldCount, multiple);
    }

    public static Event KILL_ZOMBIE(int killCount, boolean multiple) {
        return new Event(Type.KILL_ZOMBIE, killCount, multiple);
    }

    public static Event KILL_HERO(int killCount, boolean multiple) {
        return new Event(Type.KILL_HERO, killCount, multiple);
    }

    public static Event LOSE(boolean multiple) {
        return new Event(multiple);
    }

    public enum Type {
        WIN,
        LOSE,
        KILL_ZOMBIE,
        KILL_HERO;
    }

    public Event(Type type, int killCount, boolean multiple) {
        this.multiple = multiple;
        this.type = type;
        this.killCount = killCount;
    }

    public Event(int goldCount, boolean multiple) {
        this.multiple = multiple;
        type = Type.WIN;
        this.goldCount = goldCount;
    }

    public Event(boolean multiple) {
        this.multiple = multiple;
        type = Type.LOSE;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public int getGoldCount() {
        return goldCount;
    }

    public int getKillCount() {
        return killCount;
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public Event value() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Event events = (Event) o;

        if (goldCount != events.goldCount) {
            return false;
        }

        return type == events.type;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (type != null) {
            result = type.hashCode();
        }

        result = 31 * result + goldCount;
        return result;
    }

    @Override
    public String toString() {
        return type +
                "(gold=" + goldCount +
                ", kill=" + killCount +
                ", " + ((multiple)?"multiple":"single") +
                ")";
    }
}
