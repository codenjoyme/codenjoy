package com.codenjoy.dojo.web.rest.dto.settings;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.entity.server.PParameter;
import com.codenjoy.dojo.services.entity.server.PParameters;
import com.codenjoy.dojo.services.entity.server.SimpleNamedParameter;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractSettings {

    protected final List<PParameter> parameters;

    public AbstractSettings() {
        parameters = new LinkedList<>();
    }

    public AbstractSettings(PParameters parameters) {
        this.parameters = parameters.getParameters();
    }

    protected final void add(String name, Object value) {
        parameters.add(new PParameter(new SimpleNamedParameter(name, value)));
    }

    protected final Parameter<Object> find(String name, List<PParameter> input) {
        return input.stream()
                .filter(parameter -> parameter.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Parameter name not found: " + name))
                .build();
    }

    public abstract void update(List<PParameter> parameters) ;

    protected final void update(List<PParameter> input, String name) {
        try {
            Object value = find(name, parameters).getValue();
            find(name, input).update(value);
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }

    protected final Boolean getBoolean(String name) {
        return (Boolean) find(name, parameters).getValue();
    }
    
    protected final Integer getInteger(String name) {
        return (Integer) find(name, parameters).getValue();
    }

    public final PParameters parameters() {
        return new PParameters(){{
            setParameters(parameters);
        }};
    }
}
