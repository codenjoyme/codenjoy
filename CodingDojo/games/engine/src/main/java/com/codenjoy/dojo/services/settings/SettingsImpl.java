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


import java.util.*;

import static java.util.stream.Collectors.toList;

public class SettingsImpl implements Settings {

    private Map<String, Parameter<?>> map = new LinkedHashMap<>();

    @Override
    public List<Parameter> getParameters() {
        return new LinkedList<>(map.values());
    }

    @Override
    public EditBox<?> addEditBox(String name) {
        return (EditBox<?>) (map.containsKey(name)
                ? map.get(name)
                : put(name, new EditBox(name)));
    }

    @Override
    public SelectBox<?> addSelect(String name, List<Object> options) {
        return (SelectBox<?>) (map.containsKey(name)
                ? map.get(name)
                : put(name, new SelectBox(name, options)));
    }

    @Override
    public CheckBox<Boolean> addCheckBox(String name) {
        return (CheckBox<Boolean>) (map.containsKey(name)
                ? map.get(name)
                : put(name, new CheckBox<Boolean>(name).type(Boolean.class)));
    }

    private Parameter put(String name, Parameter parameter) {
        map.put(name, parameter);
        return parameter;
    }

    @Override
    public boolean hasParameter(String name) {
        return map.containsKey(name);
    }

    @Override
    public Parameter<?> getParameter(String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        }
        throw new IllegalArgumentException(String.format("Parameter with name '%s' not found", name));
    }

    @Override
    public void removeParameter(String name) {
        map.remove(name);
    }

    @Override
    public boolean changed() {
        return map.values().stream()
                .anyMatch(Parameter::changed);
    }

    @Override
    public List<String> whatChanged() {
        return map.values().stream()
                .filter(Parameter::changed)
                .map(Parameter::getName)
                .collect(toList());
    }

    @Override
    public void changesReacted() {
        map.values()
                .forEach(Parameter::changesReacted);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void updateAll(List<Parameter> parameters) {
        parameters.forEach(parameter -> {
            String name = parameter.getName();
            if (map.containsKey(name)) {
                ((Parameter<Object>) map.get(name)).update(parameter.getValue());
            } else {
                map.put(name, parameter);
            }
        });
    }
}
