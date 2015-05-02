package com.codenjoy.dojo.services.settings;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SettingsImpl implements Settings {

    private List<Parameter<?>> parameters = new LinkedList<Parameter<?>>();
    private Map<String, Parameter<?>> map = new HashMap<String, Parameter<?>>();

    @Override
    public List<Parameter<?>> getParameters() {
        return new LinkedList<Parameter<?>>(parameters);
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
}
