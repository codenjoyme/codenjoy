package com.codenjoy.dojo.services.settings;

import java.util.List;

/**
 * User: sanja
 * Date: 26.09.13
 * Time: 8:49
 */
public interface Settings {
    List<Parameter> getParameters();

    Parameter addEditBox(String name);

    Parameter addSelect(String name, List<Object> strings);

    Parameter addCheckBox(String name);

    Parameter getParameter(String name);
}
