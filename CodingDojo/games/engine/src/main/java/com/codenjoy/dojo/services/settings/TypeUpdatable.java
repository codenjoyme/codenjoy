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


import java.util.function.Function;

public abstract class TypeUpdatable<T> extends Updatable<T> implements Parameter<T> {

    protected Class<?> type;
    private Function<String, T> parser;

    @Override
    public Class<?> getValueType() {
        return (type != null) ? type :
                (getValue() != null) ? getValue().getClass() : Object.class;
    }

    @Override
    public <V> Parameter<V> type(Class<V> type) {
        this.type = type;
        return (Parameter<V>) this;
    }

    @Override
    public Parameter<T> parser(Function<String, T> parser) {
        this.parser = parser;
        return this;
    }

    protected T tryParse(Object value) {
        if (parser != null) {
            return parser.apply(String.valueOf(value));
        } else {
            throw new IllegalArgumentException(
                    String.format("Unsupported format [%s] " +
                                    "for parameter of type %s",
                            value.getClass(), value));
        }
    }
}
