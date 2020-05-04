package com.codenjoy.dojo.services.nullobj;

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


import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class NullParameter<T> implements Parameter<T> {

    public static final Parameter INSTANCE = new NullParameter();

    private NullParameter() {
        // do nothing
    }

    @Override
    public T getValue() {
        return (T)new Object();
    }

    @Override
    public String getType() {
        return StringUtils.EMPTY;
    }

    @Override
    public Class<?> getValueType() {
        return Object.class;
    }

    @Override
    public String getName() {
        return StringUtils.EMPTY;
    }

    @Override
    public Parameter<T> update(T value) {
        return null;
    }

    @Override
    public Parameter<T> def(T value) {
        return INSTANCE;
    }

    @Override
    public <V> Parameter<V> type(Class<V> type) {
        return INSTANCE;
    }

    @Override
    public Parameter<T> parser(Function<String, T> parser) {
        return INSTANCE;
    }

    @Override
    public void select(int index) {
        // do nothing
    }

    @Override
    public boolean changed() {
        return false;
    }

    @Override
    public void changesReacted() {
        // do nothing
    }

    @Override
    public List<T> getOptions() {
        return Arrays.asList();
    }

    @Override
    public T getDefault() {
        return null;
    }
}
