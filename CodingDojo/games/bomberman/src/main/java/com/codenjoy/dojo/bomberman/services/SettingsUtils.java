package com.codenjoy.dojo.bomberman.services;

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

import com.codenjoy.dojo.services.settings.Parameter;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SettingsUtils {
    public static void save(JSONObject json, Object object) {
        fields(object, Parameter.class)
                .forEach(name -> save(object, name, json));
    }

    public static void load(JSONObject json, Object object) {
        fields(object, Parameter.class)
                .forEach(name -> load(object, name, json));
    }

    private static List<String> fields(Object object, Class type) {
        return Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.getType().equals(type))
                .map(field -> field.getName())
                .collect(toList());
    }

    private static void save(Object object, String name, JSONObject json) {
        if (!json.has(name)) {
            return;
        }

        get(object, name).update(json.get(name));
    }

    private static void load(Object object, String name, JSONObject json) {
        json.put(name, get(object, name).getValue());
    }

    private static Parameter<Object> get(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);

            boolean accessible = field.isAccessible();
            field.setAccessible(true);

            Parameter<Object> result = (Parameter<Object>) field.get(object);

            field.setAccessible(accessible);
            return result;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
