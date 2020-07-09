package com.codenjoy.dojo.services.settings;

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


import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class EditBox<T> extends TypeUpdatable<T> implements Parameter<T> {

    public static final String TYPE = "editbox";

    private String name;
    private T def;
    private boolean multiline;

    public EditBox(String name) {
        this.name = name;
        multiline = false;
    }

    public boolean isMultiline() {
        return multiline;
    }

    @Override
    public T getValue() {
        return (get() == null) ? def : get();
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public EditBox<T> update(T value) {
        if (value instanceof String) {
            if (Integer.class.equals(type)) {
                set((T) Integer.valueOf((String) value));
            } else if (Boolean.class.equals(type)) {
                set((T) Boolean.valueOf((String) value));
            } else if (Double.class.equals(type)) {
                set((T) Double.valueOf((String) value));
            } else if (String.class.equals(type)) {
                set(value);
            } else {
                set(tryParse(value));
            }
        } else {
            set(value);
        }
        return this;
    }

    @Override
    public <V> EditBox<V> type(Class<V> type) {
        return (EditBox<V>) super.type(type);
    }

    @Override
    public EditBox<T> parser(Function<String, T> parser) {
        return (EditBox<T>) super.parser(parser);
    }

    @Override
    public EditBox<T> def(T value) {
        def = value;
        return this;
    }

    public EditBox<T> multiline(boolean multiline) {
        this.multiline = multiline;
        return this;
    }

    public EditBox<T> multiline() {
        multiline = true;
        return this;
    }

    @Override
    public void select(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> getOptions() {
        return new LinkedList<T>(){{
            add(def);
            T value = EditBox.this.get();
            if (value != null && !this.contains(value)) {
                add(value);
            }
        }};
    }

    @Override
    public String toString() {
        return String.format("[%s:%s = multiline[%s] def[%s] val[%s]]",
                name,
                type.getSimpleName(),
                multiline,
                def,
                get());
    }

    @Override
    public T getDefault() {
        return def;
    }
}
