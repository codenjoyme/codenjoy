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

public class SelectBox<T> extends Updatable<Integer> implements Parameter<T> {

    public static final String TYPE = "selectbox";

    private String name;
    private List<T> options;
    private Integer def;

    public SelectBox(String name, List<T> options) {
        this.name = name;
        this.options = options;
    }

    @Override
    public T getValue() {
        return (get() == null) ?
                ((def == null) ?
                        null :
                        options.get(def)) :
                options.get(get());
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
    public Class<?> getValueType() {
        return (!options.isEmpty() && options.get(0) != null) ? options.get(0).getClass() : Object.class;
    }

    @Override
    public SelectBox<T> update(T value) {
        checkIsPresent(value);
        set(options.indexOf(value));

        return this;
    }

    private void checkIsPresent(T value) {
        if (!options.contains(value)) {
            throw new IllegalArgumentException(String.format("No option '%s' in set %s", value, options));
        }
    }

    @Override
    public SelectBox<T> def(T value) {
        checkIsPresent(value);
        this.def = options.indexOf(value);
        return this;
    }

    public <V> SelectBox<V> type(Class<V> type) {
        return (SelectBox<V>) this;
    }

    @Override
    public SelectBox<T> parser(Function<String, T> parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void select(int index) {
        set(index);
    }

    @Override
    public List<T> getOptions() {
        return new LinkedList<T>(options);
    }

    @Override
    public String toString() {
        return String.format("[%s:%s = options%s def[%s] val[%s]]",
                name,
                "String",
                options.toString(),
                def,
                get());
    }

    @Override
    public T getDefault() {
        if (def == null) {
            return null;
        }
        return options.get(def);
    }
}
