package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


public class GlassEvent<T> {

    public enum Type{
        GLASS_OVERFLOW, LINES_REMOVED, FIGURE_DROPPED, WITHOUT_OVERFLOWN_LINES_REMOVED, TOTAL_LINES_REMOVED
    }

    private Type type;
    private T value;

    public GlassEvent(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return type;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GlassEvent that = (GlassEvent) o;

        if (type != that.type) return false;

        if (value == null || that.value == null) {
            return value == that.value;
        }
        if (type.equals(Type.TOTAL_LINES_REMOVED)
            || type.equals(Type.WITHOUT_OVERFLOWN_LINES_REMOVED))
        {
            return (Integer) value <= (Integer) that.value;
        } else {
            return value.equals(that.value);
        }
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
