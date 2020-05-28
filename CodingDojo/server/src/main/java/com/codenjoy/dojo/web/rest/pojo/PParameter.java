package com.codenjoy.dojo.web.rest.pojo;

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

import com.codenjoy.dojo.services.settings.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@Setter
@NoArgsConstructor
public class PParameter {

    private String value;
    private String valueType;
    private String type;
    private String name;
    private String def;
    private boolean multiline;
    private List<String> options;

    public PParameter(Parameter p) {
        this.value = String.valueOf(p.getValue());
        this.valueType = p.getValueType().getSimpleName();
        this.type = p.getType();
        this.name = p.getName();
        this.def = String.valueOf(p.getDefault());
        this.multiline = (p instanceof EditBox) && ((EditBox) p).isMultiline();
        this.options = (List)(p.getOptions()).stream()
                .map(it -> (it == null) ? null : it.toString()) // TODO test null case
                .collect(toList());
    }

    public Parameter build() {
        switch (type) {
            case EditBox.TYPE:
                return new EditBox<>(name)
                        .type(type(valueType))
                        .multiline(multiline)
                        .def(def)
                        .update(value);

            case CheckBox.TYPE:
                return new CheckBox<>(name)
                        .type(type(valueType))
                        .def(def)
                        .update(value);

            case SelectBox.TYPE:
                return new SelectBox(name, options)
                        .type(type(valueType))
                        .def(def)
                        .update(value);

            default:
                return new SimpleParameter(name)
                        .type(type(valueType))
                        .def(def)
                        .update(value);
        }
    }

    public static Class type(String type) {
        switch (type) {
            case "Integer": return Integer.class;
            case "String": return String.class;
            case "Boolean": return Boolean.class;
            default: return String.class;
        }
    }

}
