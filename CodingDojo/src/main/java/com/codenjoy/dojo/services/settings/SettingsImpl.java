package com.codenjoy.dojo.services.settings;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 8:49
 */
public class SettingsImpl implements Settings {

    private List<Parameter> parameters = new LinkedList<Parameter>();

    @Override
    public List<Parameter> getParameters() {
        return new LinkedList<Parameter>(parameters);
    }

    @Override
    public Parameter addEditBox(String name) {
        Parameter parameter = new EditBox(name);
        parameters.add(parameter);
        return parameter;
    }

    @Override
    public Parameter addSelect(String name, List<Object> strings) {
        Parameter parameter = new SelectBox(name, strings);
        parameters.add(parameter);
        return parameter;
    }

    @Override
    public Parameter addCheckBox(String name) {
        Parameter parameter = new CheckBox(name);
        parameters.add(parameter);
        return parameter;
    }

    @Override
    public Parameter getParameter(String name) {
        for (Parameter p : parameters) {
            if (p.itsMe(name)) {
                return p;
            }
        }
        throw new IllegalArgumentException(String.format("Parameter with name %s nt found", name));
    }
}
