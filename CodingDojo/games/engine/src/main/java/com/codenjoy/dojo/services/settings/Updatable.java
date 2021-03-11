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

import java.util.function.Consumer;

public abstract class Updatable<T> {

    protected T value;
    protected boolean changed = false;
    private Consumer<T> onChange;

    protected T get() {
        return value;
    }

    public void justSet(T value) {
        changed = ((this.value == null && value != null) || (this.value != null && !this.value.equals(value)));
        this.value = value;
    }

    protected void set(T value) {
        justSet(value);
        if (onChange != null) {
            onChange.accept(value);
        }
    }

    public Parameter onChange(Consumer consumer) {
        this.onChange = consumer;
        return (Parameter<T>) this;
    }

    public boolean changed() {
        return changed;
    }

    public void changesReacted() {
        changed = false;
    }
}
