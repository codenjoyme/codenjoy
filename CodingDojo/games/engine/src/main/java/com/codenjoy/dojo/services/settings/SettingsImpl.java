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


import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SettingsImpl implements Settings {

    private List<Parameter<?>> parameters = new LinkedList<>();
    private Map<String, Parameter<?>> map = new HashMap<>();

    @Override
    public List<Parameter<?>> getParameters() {
        return new LinkedList<>(parameters);
    }

    @Override
    public Parameter<?> addEditBox(String name) {
        if (map.containsKey(name)) return map.get(name);

        Parameter<?> parameter = new EditBox(name);
        parameters.add(parameter);
        map.put(name, parameter);
        return parameter;
    }

    @Override
    public Parameter<?> addSelect(String name, List<Object> strings) {
        if (map.containsKey(name)) return map.get(name);

        Parameter<?> parameter = new SelectBox(name, strings);
        parameters.add(parameter);
        map.put(name, parameter);
        return parameter;
    }

    @Override
    public Parameter<Boolean> addCheckBox(String name) {
        if (map.containsKey(name)) return (Parameter<Boolean>) map.get(name);

        Parameter<Boolean> parameter = new CheckBox(name);
        parameter.type(Boolean.class);
        parameters.add(parameter);
        map.put(name, parameter);
        return parameter;
    }

    @Override
    public Parameter<?> getParameter(String name) {
        for (Parameter<?> p : parameters) {
            if (p.itsMe(name)) {
                return p;
            }
        }
        throw new IllegalArgumentException(String.format("Parameter with name '%s' not found", name));
    }

    @Override
    public void removeParameter(String name) { // TODO test me
        for (Parameter<?> p : parameters.toArray(new Parameter[0])) {
            if (p.itsMe(name)) {
                parameters.remove(p);
                return;
            }
        }
        throw new IllegalArgumentException(String.format("Parameter with name '%s' not found", name));
    }

    @Override
    public boolean changed() {
        boolean result = false;
        for (Parameter<?> parameter : parameters) {
            result |= parameter.changed();
        }
        return result;
    }

    @Override
    public List<String> whatChanged() {
        List<String> result = new LinkedList<>();
        for (Parameter<?> parameter : parameters) {
            if (parameter.changed()) {
                result.add(parameter.getName());
            }
        }
        return result;
    }

    @Override
    public void changesReacted() {
        for (Parameter<?> parameter : parameters) {
            parameter.changesReacted();
        }
    }
}
