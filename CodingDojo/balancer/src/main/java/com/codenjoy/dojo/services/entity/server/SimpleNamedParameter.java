package com.codenjoy.dojo.services.entity.server;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import lombok.Getter;

@Getter
public class SimpleNamedParameter extends SimpleParameter {

    private String name;
    private Class type;
    private Object def;

    public SimpleNamedParameter(String name) {
        this(name, null);
    }

    public SimpleNamedParameter(String name, Object value) {
        super(value);
        this.name = name;

        if (value == null) {
            this.type(String.class);
        } else {
            this.type(value.getClass());
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Parameter type(Class type) {
        this.type = type;
        return this;
    }

    @Override
    public String getType() {
        switch (type.getSimpleName()) {
            case "Integer": return EditBox.TYPE;
            case "String": return EditBox.TYPE;
            case "Boolean": return CheckBox.TYPE;
            default: return EditBox.TYPE;
        }
    }

    @Override
    public Parameter def(Object value) {
        this.def = value;
        return this;
    }

    @Override
    public Object getDefault() {
        return def;
    }

}
