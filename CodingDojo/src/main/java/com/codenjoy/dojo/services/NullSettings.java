package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

import java.util.LinkedList;
import java.util.List;

/**
* User: sanja
* Date: 21.11.13
* Time: 13:33
*/
public class NullSettings implements Settings {
    @Override
    public List<Parameter<?>> getParameters() {
        return new LinkedList<Parameter<?>>();
    }

    @Override
    public Parameter<?> addEditBox(String name) {
        return null;
    }

    @Override
    public Parameter<?> addSelect(String name, List<Object> strings) {
        return null;
    }

    @Override
    public Parameter<Boolean> addCheckBox(String name) {
        return null;
    }

    @Override
    public Parameter<?> getParameter(String name) {
        return null;
    }
}
