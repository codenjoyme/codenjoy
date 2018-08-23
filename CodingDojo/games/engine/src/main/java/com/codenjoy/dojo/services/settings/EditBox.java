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

public class EditBox<T> extends TypeUpdatable<T> implements Parameter<T> {

    private String name;
    private T def;

    public EditBox(String name) {
        this.name = name;
    }

    @Override
    public T getValue() {
        return (get() == null) ? def : get();
    }

    @Override
    public String getType() {
        return "editbox";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(T value) {
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
    }

    @Override
    public Parameter<T> def(T value) {
        def = value;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }

    @Override
    public void select(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> getOptions() {
        return new LinkedList<T>(){{
            add(def);
            if (EditBox.this.get() != null) {
                add(EditBox.this.get());
            }
        }};
    }

    @Override
    public String toString() { // TODO test me and add this method to all classes
        return String.format("%s:%s = def[%s] val[%s]",
                name,
                type.getSimpleName(),
                def,
                get());
    }
}
