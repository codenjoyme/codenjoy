package com.codenjoy.dojo.tetris.services;

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
    private int level;
    private int data;

    public static Event glassOverflown(int levelNumber) {
        return new Event(Type.GLASS_OVERFLOWN, levelNumber, 0);
    }

    public static Event linesRemoved(int levelNumber, int removedLines) {
        return new Event(Type.LINES_REMOVED, levelNumber, removedLines);
    }

    public static Event figuresDropped(int levelNumber, int figureIndex) {
        return new Event(Type.FIGURES_DROPPED, levelNumber, figureIndex);
    }

    public enum Type {
        GLASS_OVERFLOWN,
        LINES_REMOVED,
        FIGURES_DROPPED;
    }

    public Event(Type type, int level, int data) {
        this.type = type;
        this.level = level;
        this.data = data;
    }

    public boolean isGlassOverflown() {
        return type.equals(Type.GLASS_OVERFLOWN);
    }

    public boolean isLinesRemoved() {
        return type.equals(Type.LINES_REMOVED);
    }

    public boolean isFiguresDropped() {
        return type.equals(Type.FIGURES_DROPPED);
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("Event[%s:%s:%s]", type, level, data);
    }

    public int getLevel() {
        return level;
    }

    public int getFigureIndex() {
        return data;
    }

    public int getRemovedLines() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event events = (Event) o;

        if (level != events.level) return false;
        if (data != events.data) return false;
        return type.equals(events.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
