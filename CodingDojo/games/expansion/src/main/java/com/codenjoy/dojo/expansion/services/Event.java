package com.codenjoy.dojo.expansion.services;

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

public class Event {

    public static Event WIN(int score) {
        return new Event(score);
    }

    public static Event LOSE() {
        return new Event();
    }

    public enum Type {
        WIN, LOSE;

    }
    private Type type;

    private int score;

    public Event(int score) {
        type = Type.WIN;
        this.score = score;
    }

    public Event() {
        type = Type.LOSE;
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public Type getType() {
        return type;
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

        if (score != events.score) {
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

        result = 31 * result + score;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Event{%s=%s}",
                type, score);
    }
}
