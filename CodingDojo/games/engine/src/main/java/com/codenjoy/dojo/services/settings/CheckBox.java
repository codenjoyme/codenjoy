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

public class CheckBox<T> extends TypeUpdatable<T> implements Parameter<T> {

    private T def;
    private String name;

    public CheckBox(String name) {
        this.name = name;
    }

    @Override
    public T getValue() {
        return (get() == null) ? def : get();
    }

    @Override
    public String getType() {
        return "checkbox";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(T value) {
        if (value == null) {
            return;
        }
        Boolean b = parse(value);
        if (b == null) {
            set(tryParse(value));
        } else {
            set(code(b));
        }
    }

    private T code(boolean value) {
        if (Integer.class.equals(type)) {
            return (T)((value) ? Integer.valueOf(1) : Integer.valueOf(0));
        } else if (Boolean.class.equals(type)) {
            return (T) Boolean.valueOf(value);
        } else if (String.class.equals(type)) {
            return (T) Boolean.valueOf(value).toString();
        } else {
            return tryParse(Boolean.valueOf(value));
        }
    }

    private Boolean parse(T value) {
        if (value instanceof Boolean) {
            return (Boolean)value;
        } else if (value instanceof String) {
            return ("true".equalsIgnoreCase((String) value)
                    || "1".equals(value));
        } else if (value instanceof Integer){
            return ((Integer)value == 1);
        } else {
            return null;
        }
    }

    @Override
    public Parameter<T> def(T value) {
        this.def = value;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }

    @Override
    public void select(int index) {
        set((T) Boolean.valueOf(index == 1));
    }

    @Override
    public List<T> getOptions() {
        return new LinkedList<T>(){{
            add(def);
            if (CheckBox.this.get() != null) {
                add(CheckBox.this.get());
            }
        }};
    }


}
