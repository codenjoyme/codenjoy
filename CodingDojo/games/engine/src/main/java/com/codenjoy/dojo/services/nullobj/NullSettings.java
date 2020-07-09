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


import com.codenjoy.dojo.services.settings.*;

import java.util.LinkedList;
import java.util.List;

public final class NullSettings implements Settings {

    public static final Settings INSTANCE = new NullSettings();

    private NullSettings() {
        // do nothing
    }

    @Override
    public List<Parameter> getParameters() {
        return new LinkedList<>();
    }

    @Override
    public EditBox<?> addEditBox(String name) {
        return null;
    }

    @Override
    public SelectBox<?> addSelect(String name, List<Object> strings) {
        return null;
    }

    @Override
    public CheckBox<Boolean> addCheckBox(String name) {
        return null;
    }

    @Override
    public boolean hasParameter(String name) {
        return false;
    }

    @Override
    public void removeParameter(String name) {
        // do nothing
    }

    @Override
    public Parameter<?> getParameter(String name) {
        return null;
    }

    @Override
    public boolean changed() {
        return false;
    }

    @Override
    public List<String> whatChanged() {
        return new LinkedList<>();
    }

    @Override
    public void changesReacted() {
        // do nothing
    }

    @Override
    public void clear() {
        // do nothing
    }

    @Override
    public void updateAll(List<Parameter> parameters) {
        // do nothing
    }
}
