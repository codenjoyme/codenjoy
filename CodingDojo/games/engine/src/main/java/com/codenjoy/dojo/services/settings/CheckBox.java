package com.codenjoy.dojo.services.settings;

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


import java.util.function.Function;

public class CheckBox extends Updatable<Boolean> implements Parameter<Boolean> {
    private Boolean def;
    private String name;

    public CheckBox(String name) {
        this.name = name;
    }

    @Override
    public Boolean getValue() {
        return (get() == null) ? def : get();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void update(Boolean value) {
        set(value);
    }

    @Override
    public Parameter<Boolean> def(Boolean value) {
        this.def = value;
        return this;
    }

    @Override
    public boolean itsMe(String name) {
        return this.name.equals(name);
    }

    public <V> Parameter<V> type(Class<V> integerClass) {
        return (Parameter<V>)this;
    }

    @Override
    public Parameter<Boolean> parser(Function<String, Boolean> parser) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void select(int index) {
        set(index == 1);
    }


}
